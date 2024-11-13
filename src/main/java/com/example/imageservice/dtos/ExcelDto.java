package com.example.imageservice.dtos;

import com.example.imageservice.entities.enums.ProcessStatus;
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

  @NotEmpty private MultipartFile fileResult;

  private String objectName;

  private Integer count = 0;

  private Integer success = 0;

  private Integer error = 0;

  private Long process;

  private Long userId;

  private String description;
}
