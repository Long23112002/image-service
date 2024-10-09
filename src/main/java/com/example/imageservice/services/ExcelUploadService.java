package com.example.imageservice.services;

import com.example.imageservice.dtos.ExcelDto;
import com.example.imageservice.entities.FileUpload;
import com.example.imageservice.repositories.FileUploadRepository;
import com.example.imageservice.utils.Slug;
import com.longnh.exceptions.ExceptionHandle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ExcelUploadService {

  @Value("${excel.dir}")
  private String uploadDir;

  @Value("${url.port}")
  private String portServer;

  @Value("${url.host}")
  private String hostServer;

  @Autowired private FileUploadRepository fileUploadRepository;

  public FileUpload save(ExcelDto dto) throws IOException {
    if (!Objects.requireNonNull(dto.getFile().getOriginalFilename()).endsWith(".xlsx")) {
      throw new ExceptionHandle(
          HttpStatus.BAD_REQUEST, "Chỉ cho phép upload file Excel có định dạng .xlsx");
    }

    String originalFileName = dto.getFile().getOriginalFilename();
    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
    String baseFileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));

    String slugFileName = Slug.makeSlug(baseFileName);

    String fileName = slugFileName + fileExtension;
    Path filePath = Paths.get(uploadDir + File.separator + fileName);
    Files.createDirectories(filePath.getParent());

    try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
      fos.write(dto.getFile().getBytes());
    }

    String fileDownloadUri = hostServer + ":" + portServer + "/api/v1/files/download/" + fileName;

    FileUpload fileUpload = new FileUpload();
    fileUpload.setUrl(fileDownloadUri);
    fileUpload.setObjectId(dto.getObjectId());
    fileUpload.setObjectName(dto.getObjectName());
    fileUpload.setType(dto.getType());

    return fileUploadRepository.save(fileUpload);
  }

  public byte[] download(String fileName) throws IOException {
    Path filePath = Paths.get(uploadDir + File.separator + fileName);
    return Files.readAllBytes(filePath);
  }
}
