package com.example.imageservice.services;

import com.example.imageservice.dtos.ImageDto;
import com.example.imageservice.dtos.ImageResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUploadService {

  @Autowired private ImageRepository imageRepository;

  @Value("${upload.dir}")
  private String uploadDir;

  @Value("${url.server}")
  private String urlServer;

  @Value("${url.port}")
  private String portServer;

  public ImageResponse uploadImage(ImageDto dto) throws IOException {
    List<Image> images = new ArrayList<>();

    String code = upLoadToServer(dto.getFile());
    for (MultipartFile file : dto.getFile()) {
      if (file != null && !file.isEmpty()) {
        Image image =
            Image.builder()
                .url(urlServer + ":" + portServer + "/" + code + "-" + file.getOriginalFilename())
                .objectId(dto.getObjectId())
                .objectName(dto.getObjectName())
                .build();

        images.add(imageRepository.save(image));
      }
    }

    List<com.example.imageservice.entities.value.File> files = new ArrayList<>();
    for (Image image : images) {
      File file = new File();
      file.setUrl(image.getUrl());
      files.add(file);
    }

    ImageResponse response = new ImageResponse();
    response.setFile(files);
    response.setObjectId(dto.getObjectId());
    response.setObjectName(dto.getObjectName());
    response.setDeleted(false);

    logUploadedFiles();

    return response;
  }

  private String upLoadToServer(List<MultipartFile> files) throws IOException {
    if (files == null || files.isEmpty()) {
      throw new ExceptionHandle(HttpStatus.BAD_REQUEST, ErrorMessage.FILE_NOT_FOUND);
    }

    Path path = Paths.get(uploadDir);
    if (!Files.exists(path)) {
      Files.createDirectories(path);
    }
    String code = null;
    for (MultipartFile file : files) {

      if (file != null && !file.isEmpty()) {
        code = RandomStringUtils.randomAlphanumeric(10);
        String originalFilename = file.getOriginalFilename();
        Path fileSave = path.resolve(code + "-" + Objects.requireNonNull(originalFilename));
        try (InputStream is = file.getInputStream()) {
          Files.copy(is, fileSave, StandardCopyOption.REPLACE_EXISTING);
        }
      }
    }

    return code;
  }


  private void logUploadedFiles() {
    try {
      Path path = Paths.get(uploadDir);
      long count = Files.list(path).count();
      System.out.println("Number of files uploaded: " + count);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
