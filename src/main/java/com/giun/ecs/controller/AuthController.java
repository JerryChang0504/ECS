package com.giun.ecs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giun.ecs.dto.request.ChangePswRequest;
import com.giun.ecs.dto.request.LoginRequest;
import com.giun.ecs.dto.request.RegisterRequest;
import com.giun.ecs.dto.request.UpdateUserRequest;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.exception.ApplicationException;
import com.giun.ecs.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Authentication", description = "使用者認證相關 API")
public class AuthController extends BaseController {

	@Autowired
	private AuthService authService;

	/**
	 * 使用者註冊
	 * 
	 * @param req
	 * @return
	 * @throws ApplicationException
	 */
	@PostMapping("/register")
	@Operation(summary = "使用者註冊", description = "建立新使用者帳號")
	public ResponseEntity<Outbound> register(
			@Valid @RequestBody RegisterRequest req)
			throws ApplicationException {
		Outbound response = authService.register(req);
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}

	/**
	 * 使用者登入
	 * 
	 * @param request
	 * @return
	 * @throws ApplicationException
	 */
	@PostMapping("/login")
	@Operation(summary = "使用者登入", description = "使用帳號密碼登入，成功回傳 JWT Token")
	public ResponseEntity<Outbound> login(
			@Valid @RequestBody LoginRequest request)
			throws ApplicationException {
		Outbound res = authService.login(request);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	/**
	 * 取得使用者資料
	 * 
	 * @param Bearertoken
	 * @return
	 * @throws ApplicationException
	 */
	@GetMapping("/finduser")
	public ResponseEntity<Outbound> getUser(
			@RequestHeader(name = "Authorization") String Bearertoken)
			throws ApplicationException {
		String token = extractBearerToken(Bearertoken);
		Outbound response = authService.findUserByUsername(token);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 更新使用者資料
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/profile")
	@Operation(summary = "更新使用者資料")
	public ResponseEntity<Outbound> updateUserProfile(
			@Valid @RequestBody UpdateUserRequest request) throws Exception {
		Outbound response = authService.updateUserProfile(request.getUsername(),
				request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 取得目前使用者資料
	 * 
	 * @return
	 */
	@GetMapping("/user")
	@Operation(summary = "取得目前使用者資料")
	public ResponseEntity<Outbound> getCurrentUser() {
		Outbound response = authService.getCurrentUser();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 修改密碼
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/change-password")
	@Operation(summary = "修改密碼")
	public ResponseEntity<Outbound> changePassword(
			@Valid @RequestBody ChangePswRequest request) throws Exception {
		Outbound response = authService.updatePassword(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	/**
	 * 登出使用者
	 */
	@PostMapping("/logout")
	public ResponseEntity<Outbound> logout() {
		Outbound response = authService.logout();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
