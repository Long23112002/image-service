package com.example.imageservice.controllers;

import com.example.imageservice.dtos.ExcelDto;
import com.example.imageservice.entities.FileUpload;
import com.example.imageservice.services.ExcelUploadService;
import java.io.IOException;

import com.example.imageservice.utils.ResponsePageV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/files")
public class FileUploadController {

  @Autowired private ExcelUploadService excelUploadService;

  @PostMapping("/upload")
  public void upload(@ModelAttribute ExcelDto dto) throws IOException {
    excelUploadService.save(dto);
  }

  @GetMapping("/download/{fileName}")
  public ResponseEntity<byte[]> downloadExcelFile(@PathVariable String fileName) {
    try {
      byte[] data = excelUploadService.download(fileName);
      return ResponseEntity.ok()
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
          .body(data);
    } catch (IOException e) {
      return ResponseEntity.status(404).body(null);
    }
  }

  @GetMapping
  public ResponsePageV2<FileUpload> filter(Pageable pageable){
    return new ResponsePageV2<>(excelUploadService.filter(pageable));
  }
}
