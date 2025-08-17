package com.giun.ecs.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登入請求")
public class LoginReq {
  @NotBlank
  @Schema(description = "使用者帳號", example = "john_doe")
  private String username;

  @NotBlank
  @Schema(description = "使用者密碼", example = "P@ssw0rd123")
  private String password;
}
