package com.example.imageservice.dtos;


import com.example.imageservice.entities.value.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelDto {

  @NotEmpty private MultipartFile file;

  @NotEmpty private MultipartFile fileResult;

  @NotBlank
  private String objectName;

  private Integer count = 0;

  private Integer success = 0;

  private Integer error = 0;

  private String typeFile;

  private Long process;

  private String userInfo;

  private String description;
}
