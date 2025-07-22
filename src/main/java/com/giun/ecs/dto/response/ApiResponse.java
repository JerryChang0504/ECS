package com.giun.ecs.dto.response;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "通用 API 回應格式")
public class ApiResponse<T> {
  @Schema(description = "HTTP 狀態碼", example = "200")
  private int httpCode;

  @Schema(description = "狀態文字", example = "success")
  private String status;

  @Schema(description = "回傳訊息", example = "成功/失敗訊息")
  private String message;

  @Schema(description = "回傳資料")
  private T data;

  public static <T> ApiResponse<T> success(String message, T data) {
    return ApiResponse.<T>builder().httpCode(HttpStatus.OK.value())
        .status("success").message(message).data(data).build();
  }

  public static <T> ApiResponse<T> success(T data) {
    return ApiResponse.<T>builder().httpCode(HttpStatus.OK.value())
        .status("success").message("成功").data(data).build();
  }

  public static <T> ApiResponse<T> error(String message, HttpStatus status) {
    return ApiResponse.<T>builder().httpCode(status.value()).status("error")
        .message(message).data(null).build();
  }
}
