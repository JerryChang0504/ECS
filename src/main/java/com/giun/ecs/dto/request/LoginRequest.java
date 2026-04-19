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
public class LoginRequest {
  @NotBlank
  @Schema(description = "使用者帳號", example = "admin0419")
  private String username;

  @NotBlank
  @Schema(description = "使用者密碼", example = "admin0419")
  private String password;
}
