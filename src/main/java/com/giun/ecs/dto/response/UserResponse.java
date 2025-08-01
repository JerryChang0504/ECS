package com.giun.ecs.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "使用者資料回應")
public class UserResponse {
  @Schema(description = "使用者ID", example = "1")
  private Long id;

  @Schema(description = "帳號", example = "john_doe")
  private String username;

  @Schema(description = "電子郵件", example = "john@example.com")
  private String email;

  @Schema(description = "姓名", example = "John Doe")
  private String fullName;

  @Schema(description = "電話", example = "0912345678")
  private String phone;

  @Schema(description = "角色", example = "user")
  private String role;

  @Schema(description = "建立時間")
  private LocalDateTime createdAt;
}
