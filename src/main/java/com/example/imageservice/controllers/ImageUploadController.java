package com.example.imageservice.controllers;

import com.example.imageservice.dtos.ImageDto;
import com.example.imageservice.dtos.ImageResponse;
import com.example.imageservice.dtos.ResponsePage;
import com.example.imageservice.dtos.filter.ImageParam;
import com.example.imageservice.entities.Image;
import com.example.imageservice.services.ImageUploadService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/images")
public class ImageUploadController {

  @Autowired private ImageUploadService imageUploadService;

  @PostMapping
  public ResponseEntity<ImageResponse> uploadImage(@ModelAttribute ImageDto dto)
      throws IOException {
    return ResponseEntity.ok(imageUploadService.uploadImage(dto));
  }

  @GetMapping
  public ResponsePage<Image> filter(ImageParam param, Pageable pageable) {
    return new ResponsePage<>(imageUploadService.filter(param, pageable));
  }

}
