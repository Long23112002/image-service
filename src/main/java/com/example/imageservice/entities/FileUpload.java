package com.example.imageservice.entities;

import com.example.imageservice.entities.enums.ProcessStatus;
import com.example.imageservice.entities.enums.TypeFile;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;

import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "import_process")
public class FileUpload {

  @Transient public static final String SEQUENCE = "import_process_seq";

  @Id private Long id;

  @Indexed
  private Boolean isDelete = false;

  private String objectName;

  private ProcessStatus status;

  private String filePath;

  private String fileName;

  private String fileResult;

  private Integer count = 0;

  private Integer success = 0;

  private Integer error = 0;

  private Long userId;

  private Long process;

  private String description;

  private Date createdAt;

  private Date updatedAt;
}
