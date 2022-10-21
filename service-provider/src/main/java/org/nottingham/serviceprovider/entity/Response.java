package org.nottingham.serviceprovider.entity;

import lombok.Data;

@Data
public class Response<T> {

  private int code;
  private String message;
  private T data;

  public Response(int code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }
}
