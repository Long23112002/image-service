package com.example.imageservice.dtos.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ImageParam {

    private Long objectId;
    private String objectName;

}
