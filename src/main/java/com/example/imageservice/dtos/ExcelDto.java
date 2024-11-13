package com.example.imageservice.dtos;


import com.example.imageservice.entities.enums.TypeFile;
import com.example.imageservice.entities.value.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Type;

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

  @NotNull
  private TypeFile typeFile;

  @NotNull
  private Long process;

  @NotNull
  private UserInfo userInfo;

  private String description;
}
