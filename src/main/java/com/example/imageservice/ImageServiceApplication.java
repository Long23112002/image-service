package com.example.imageservice;

import com.longnh.annotions.EnableCommon;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCommon
public class ImageServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ImageServiceApplication.class, args);
  }
}
