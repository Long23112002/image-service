package com.example.imageservice.services;

import com.example.imageservice.config.SequenceGenerator;
import com.example.imageservice.dtos.ExcelDto;
import com.example.imageservice.entities.FileUpload;
import com.example.imageservice.entities.enums.ProcessStatus;
import com.example.imageservice.entities.value.UserInfo;
import com.example.imageservice.repositories.FileUploadRepository;
import com.example.imageservice.utils.Slug;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longnh.exceptions.ExceptionHandle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ExcelUploadService {

  @Value("${excel.dir}")
  private String uploadDir;

  @Value("${url.port}")
  private String portServer;

  @Value("${url.host}")
  private String hostServer;

  @Autowired private SequenceGenerator sequenceGenerator;

  @Autowired private FileUploadRepository fileUploadRepository;

  @Autowired private MongoTemplate mongoTemplate;

  public void save(ExcelDto dto) throws IOException {

    if (!Objects.requireNonNull(dto.getFile().getOriginalFilename()).endsWith(".xlsx")) {
      throw new ExceptionHandle(HttpStatus.BAD_REQUEST, "Chỉ cho phép upload file Excel có định dạng .xlsx");
    }


    if (dto.getFileResult() == null || dto.getFileResult().isEmpty()) {
      throw new ExceptionHandle(HttpStatus.BAD_REQUEST, "File kết quả không được bỏ trống");
    }

    String originalFileName = dto.getFile().getOriginalFilename();
    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
    String baseFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));


    String uniqueSuffix = UUID.randomUUID().toString();
    String fileName = baseFileName + "-" + uniqueSuffix + fileExtension;
    String resultFileName = baseFileName + "-(result)-" + uniqueSuffix + fileExtension;

    Path filePath = Paths.get(uploadDir + File.separator + fileName);
    Path resultFilePath = Paths.get(uploadDir + File.separator + resultFileName);
    Files.createDirectories(filePath.getParent());

    try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
      fos.write(dto.getFile().getBytes());
    }
    try (FileOutputStream fos = new FileOutputStream(resultFilePath.toFile())) {
      fos.write(dto.getFileResult().getBytes());
    }

    String fileDownloadUri = hostServer + ":" + portServer + "/api/v1/files/download/" + fileName;
    String fileResultDownloadUri = hostServer + ":" + portServer + "/api/v1/files/download/" + resultFileName;
    FileUpload fileUpload = new FileUpload();
    fileUpload.setId(sequenceGenerator.generateSequence(FileUpload.SEQUENCE));
    fileUpload.setFileName(fileName);
    fileUpload.setFilePath(fileDownloadUri);
    fileUpload.setFileResult(fileResultDownloadUri);
    fileUpload.setCount(dto.getCount());
    fileUpload.setError(dto.getError());
    fileUpload.setSuccess(dto.getSuccess());
    fileUpload.setStatus(ProcessStatus.IN_PROCESS);
    fileUpload.setIsDelete(false);
    fileUpload.setDescription(dto.getDescription());
    fileUpload.setUserInfo(convertJsonToUserInfo(dto.getUserInfo()));
    fileUpload.setProcess(dto.getProcess());
    fileUpload.setCreatedAt(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
   fileUpload.setTypeFile(dto.getTypeFile());
     fileUploadRepository.save(fileUpload);
  }


  public byte[] download(String fileName) throws IOException {
    Path filePath = Paths.get(uploadDir + File.separator + fileName);
    return Files.readAllBytes(filePath);
  }

  @Scheduled(cron = "*/30 * * * * *")
  public void updateStatusByProcess100() {
    Query query = new Query(Criteria.where("process").is(100));
    long count = mongoTemplate.count(query, FileUpload.class);
    if (count > 0) {
      Update update = new Update().set("status", ProcessStatus.SUCCESS);
      mongoTemplate.updateMulti(query, update, FileUpload.class);
    }
  }

  public Page<FileUpload> filter(Pageable pageable) {
    Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.asc("id")));
    return fileUploadRepository.findAll(pageableWithSort);
  }


  public UserInfo convertJsonToUserInfo(String jsonData) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    return objectMapper.readValue(jsonData, UserInfo.class);
  }

}
