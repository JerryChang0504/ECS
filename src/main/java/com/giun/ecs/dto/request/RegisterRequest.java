package com.giun.ecs.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "使用者註冊請求資料")
public class RegisterRequest {
	@NotBlank
	@Schema(description = "使用者帳號", example = "john_doe")
	public String username;

	@NotBlank
	@Schema(description = "使用者密碼", example = "P@ssw0rd123")
	public String password;

	@Email
	@NotBlank
	@Schema(description = "電子郵件", example = "john@example.com")
	public String email;

	@Schema(description = "使用者姓名", example = "John Doe")
	public String fullName;

	@Schema(description = "電話號碼", example = "0912345678")
	public String phone;
}
