package com.example.imageservice.services;

import com.example.imageservice.dtos.ImageDto;
import com.example.imageservice.dtos.ImageResponse;
import com.example.imageservice.dtos.ResponsePage;
import com.example.imageservice.dtos.filter.ImageParam;
import com.example.imageservice.entities.Image;
import com.example.imageservice.entities.value.File;
import com.example.imageservice.exceptions.ErrorMessage;
import com.example.imageservice.repositories.ImageRepository;
import com.longnh.exceptions.ExceptionHandle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUploadService {

  @Autowired private ImageRepository imageRepository;

  @Value("${upload.dir}")
  private String uploadDir;

  @Value("${url.port}")
  private String portServer;

  @Value("${url.host}")
  private String hostServer;

  public ImageResponse uploadImage(ImageDto dto) throws IOException {
    List<Image> images = new ArrayList<>();
    List<com.example.imageservice.entities.value.File> files = new ArrayList<>();

    if (dto.getFile() == null || dto.getFile().isEmpty()) {
      throw new ExceptionHandle(HttpStatus.BAD_REQUEST, ErrorMessage.FILE_NOT_FOUND);
    }

    List<String> codes = upLoadToServer(dto.getFile());

    for (int i = 0; i < dto.getFile().size(); i++) {
      MultipartFile file = dto.getFile().get(i);
      if (file != null && !file.isEmpty()) {
        String originalFilename = file.getOriginalFilename();
        String code = codes.get(i);

        Image image =
            createAndSaveImage(code, originalFilename, dto.getObjectId(), dto.getObjectName());
        images.add(image);

        File fileResponse = new File();
        fileResponse.setUrl(image.getUrl());
        files.add(fileResponse);
      }
    }

    ImageResponse response = new ImageResponse();
    response.setFile(files);
    response.setObjectId(dto.getObjectId());
    response.setObjectName(dto.getObjectName());
    response.setDeleted(false);

    return response;
  }

  public Page<Image> filter(ImageParam imageParam, Pageable pageable) {
    return imageRepository.filter(imageParam, pageable);
  }

  private List<String> upLoadToServer(List<MultipartFile> files) throws IOException {
    if (files == null || files.isEmpty()) {
      throw new ExceptionHandle(HttpStatus.BAD_REQUEST, ErrorMessage.FILE_NOT_FOUND);
    }

    Path path = Paths.get(uploadDir);
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }

    List<String> codes = new ArrayList<>();

    for (MultipartFile file : files) {
      if (file != null && !file.isEmpty()) {
        String code = RandomStringUtils.randomAlphanumeric(10);
        String originalFilename = file.getOriginalFilename();
        Path fileSave = path.resolve(code + "-" + Objects.requireNonNull(originalFilename));
        try (InputStream is = file.getInputStream()) {
          Files.copy(is, fileSave, StandardCopyOption.REPLACE_EXISTING);
        }
        codes.add(code);
      }
    }

    return codes;
  }

  private String generateImageUrl(String code, String originalFilename) {
    return hostServer + ":" + portServer + "/" + code + "-" + originalFilename;
  }

  private Image createAndSaveImage(
      String code, String originalFilename, Long objectId, String objectName) {
    Image image =
        Image.builder()
            .url(generateImageUrl(code, originalFilename))
            .objectId(objectId)
            .objectName(objectName)
            .deleted(false)
            .build();
    return imageRepository.save(image);
  }
}
