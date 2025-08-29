package com.giun.ecs.exception;

import com.giun.ecs.enums.ResultCode;
import lombok.Data;

@Data
public class ApplicationException extends Exception {

  private String code;
  private String msg;

  public ApplicationException(
      ResultCode resultCode) {
    super(resultCode.getMsg());
    this.code = resultCode.getCode();
    this.msg = resultCode.getMsg();
  }

  public ApplicationException(
      String msg) {
    super(msg);
    this.code = ResultCode.FAIL.getCode();
    this.msg = msg;
  }
}
