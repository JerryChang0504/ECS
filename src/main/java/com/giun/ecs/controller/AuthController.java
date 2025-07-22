package com.giun.ecs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giun.ecs.dto.request.LoginRequest;
import com.giun.ecs.dto.request.RegisterRequest;
import com.giun.ecs.dto.request.UpdateUserRequest;
import com.giun.ecs.dto.response.ApiResponse;
import com.giun.ecs.dto.response.JwtResponse;
import com.giun.ecs.dto.response.UserResponse;
import com.giun.ecs.entity.UserInfo;
import com.giun.ecs.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Authentication", description = "使用者認證相關 API")
public class AuthController {

  @Autowired
  private AuthService authService;

  /**
   * 使用者註冊
   * 
   * @param req
   * @return
   */

  @PostMapping("/register")
  @Operation(summary = "使用者註冊", description = "建立新使用者帳號")
  public ResponseEntity<ApiResponse<String>> register(
      @Valid @RequestBody RegisterRequest req) {
    ApiResponse<String> response = authService.register(req);
    return ResponseEntity.status(response.getHttpCode()).body(response);

  }

  /**
   * 使用者登入
   * 
   * @param request
   * @return
   */
  @PostMapping("/login")
  @Operation(summary = "使用者登入", description = "使用帳號密碼登入，成功回傳 JWT Token")
  public ResponseEntity<ApiResponse<String>> login(
      @Valid @RequestBody LoginRequest request) {
    try {
      ApiResponse<String> response = authService.login(request);
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  /**
   * 取得使用者資料
   * 
   * @param username
   * @return
   */
  @GetMapping("/user/{username}")
  public ResponseEntity<ApiResponse<UserResponse>> getUser(
      @PathVariable String username) {
    ApiResponse<UserResponse> response = authService
        .findUserByUsername(username);
    return ResponseEntity.status(response.getHttpCode()).body(response);
  }

  /**
   * 更新使用者資料
   * 
   * @param request
   * @return
   */
  @PutMapping("/profile")
  @Operation(summary = "更新使用者資料")
  public ResponseEntity<ApiResponse<UserInfo>> updateUserProfile(
      @Valid @RequestBody UpdateUserRequest request) {
    ApiResponse<UserInfo> response = authService
        .updateUserProfile(request.getUsername(), request);
    return ResponseEntity.status(response.getHttpCode()).body(response);
  }

  /**
   * 取得目前使用者資料
   * 
   * @return
   */
  @GetMapping("/user")
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "取得目前使用者資料")
  public ResponseEntity<ApiResponse<List<UserResponse>>> getCurrentUser() {
    ApiResponse<List<UserResponse>> response = authService.getCurrentUser();
    return ResponseEntity.status(response.getHttpCode()).body(response);
  }
}
