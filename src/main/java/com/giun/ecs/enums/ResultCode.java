package com.giun.ecs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
  SUCCESS("0000", "success"), FAIL("9999", "FAIL"), UNAUTHORIZED("0401",
      "UNAUTHORIZED"), FORBIDDEN("0403", "FORBIDDEN"), NOT_FOUND("0404",
          "NOT_FOUND"), VALIDATION_ERROR("0422",
              "VALIDATION_ERROR"), SERVER_ERROR("0500",
                  "SERVER_ERROR"), BAD_GATEWAY("0501",
                      "BAD_GATEWAY"), GATEWAY_TIMEOUT("0502",
                          "GATEWAY_TIMEOUT"), SERVICE_UNAVAILABLE("0503",
                              "SERVICE_UNAVAILABLE"), INTERNAL_SERVER_ERROR(
                                  "0504",
                                  "INTERNAL_SERVER_ERROR"), ACCOUNT_IS_EXIST(
                                      "0101", "帳號已存在"), EMAIL_IS_EXIST("0102",
                                          "Email 已存在"), USER_NOT_FOUND("0103",
                                              "使用者不存在"), PASSWORD_NOT_MATCH(
                                                  "0104",
                                                  "密碼不正確"), USER_IS_EXIST(
                                                      "0105",
                                                      "使用者已存在"), USER_IS_NOT_EXIST(
                                                          "0106",
                                                          "使用者不存在"), USER_IS_NOT_ACTIVE(
                                                              "0107",
                                                              "使用者未啟用"), USER_IS_NOT_AUTHORIZED(
                                                                  "0108",
                                                                  "使用者未授權"), USER_IS_NOT_AUTHENTICATED(
                                                                      "0109",
                                                                      "使用者未驗證");
  private final String code;
  private final String msg;

  public static ResultCode fromCode(String code) {
    for (ResultCode value : values()) {
      if (value.getCode().equals(value)) {
        return value;
      }
    }
    throw new IllegalArgumentException("Unknown code: " + code);
  }
}
