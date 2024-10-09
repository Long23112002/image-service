package com.example.imageservice.dtos;

import com.example.imageservice.entities.enums.TypeFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelDto {

  @NotEmpty private MultipartFile file;

  @NotNull private Long objectId;

  @NotBlank private String objectName;

  @NotNull private TypeFile type;
}
