package com.giun.ecs.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 認證令牌響應 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenResponse {
  /** 令牌類型 */
  private String tokenType;
  /** 存取令牌 */
  private String accessToken;
  /** 刷新令牌 */
  private String refreshToken;
}
