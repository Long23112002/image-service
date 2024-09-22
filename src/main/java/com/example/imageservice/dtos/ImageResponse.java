package com.example.imageservice.dtos;

import com.example.imageservice.entities.value.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponse {

    private List<File> file;
    private Long objectId;
    private String objectName;
    private Boolean deleted;
}
