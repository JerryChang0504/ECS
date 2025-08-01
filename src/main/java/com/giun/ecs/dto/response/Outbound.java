package com.giun.ecs.dto.response;

import com.giun.ecs.enums.ResultCode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Outbound {
  private String code;
  private String msg;
  private Object result;

  public static Outbound ok(Object result) {
    Outbound outbound = Outbound.builder().code(ResultCode.SUCCESS.getCode())
        .msg(ResultCode.SUCCESS.getMsg()).result(result).build();
    return outbound;
  }

  public static Outbound error(ResultCode resultCode, Object result) {
    Outbound outbound = Outbound.builder().code(resultCode.getCode())
        .msg(resultCode.getMsg()).result(result).build();
    return outbound;
  }

  public static Outbound error(ResultCode resultCode) {
    Outbound outbound = Outbound.builder().code(resultCode.getCode())
        .msg(resultCode.getMsg()).result(null).build();
    return outbound;
  }
}
