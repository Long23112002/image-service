package com.example.imageservice.exceptions;

import com.longnh.exceptions.BaseErrorMessage;

public enum ErrorMessage implements BaseErrorMessage {
  SUCCESS("Success"),
  FALSE("False"),
  FILE_NOT_FOUND("File not found"),
  INVALID_OBJECT("Invalid object"),

  ;



  public String val;

  private ErrorMessage(String label) {
    val = label;
  }

  @Override
  public String val() {
    return val;
  }
}
