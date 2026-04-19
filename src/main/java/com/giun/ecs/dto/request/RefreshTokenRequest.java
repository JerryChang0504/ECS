package com.giun.ecs.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 刷新令牌請求 DTO
 */
@Data
public class RefreshTokenRequest {
  @NotBlank(message = "refreshToken 不可為空")
  /** 刷新令牌 */
  private String refreshToken;
}
