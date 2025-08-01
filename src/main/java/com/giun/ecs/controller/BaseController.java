package com.giun.ecs.controller;

public abstract class BaseController {

  /**
   * 解析授權標頭
   * 
   * @param authHeader
   * @return
   */
  protected String extractBearerToken(String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new IllegalArgumentException("無效的授權標頭（Authorization header）");
    }
    return authHeader.substring(7).trim(); // 去除 "Bearer " 前綴並 trim
  }

  /**
   * 檢查必填參數是否為空
   * 
   * @param values
   *          必填參數
   */
  protected void checkRequired(String... values) {
    for (String value : values) {
      if (value == null || value.trim().isEmpty()) {
        throw new IllegalArgumentException("有必填參數為空");
      }
    }
  }

}
