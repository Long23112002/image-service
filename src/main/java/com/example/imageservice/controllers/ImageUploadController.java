package com.example.imageservice.controllers;

import com.example.imageservice.dtos.ImageDto;
import com.example.imageservice.dtos.ImageResponse;
import com.example.imageservice.entities.Image;
import com.example.imageservice.services.ImageUploadService;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/images")
public class ImageUploadController {

  @Autowired private ImageUploadService imageUploadService;

  @PostMapping
  public ImageResponse uploadImage(@ModelAttribute ImageDto dto) throws IOException {
    return imageUploadService.uploadImage(dto);
  }
}
