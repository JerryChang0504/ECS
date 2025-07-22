package com.giun.ecs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.giun.ecs.dto.request.LoginRequest;
import com.giun.ecs.dto.request.RegisterRequest;
import com.giun.ecs.dto.request.UpdateUserRequest;
import com.giun.ecs.dto.response.ApiResponse;
import com.giun.ecs.dto.response.UserResponse;
import com.giun.ecs.entity.UserInfo;
import com.giun.ecs.exception.ApplicationException;
import com.giun.ecs.utils.JwtUtil;

@Service
public class AuthService {

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtUtil jwtUtil;

  public ApiResponse<String> register(RegisterRequest req) {
    // 帳號與 Email 重複檢查
    if (userService.existsByUsername(req.getUsername())) {
      return ApiResponse.<String>builder()
          .httpCode(HttpStatus.BAD_REQUEST.value()).status("error")
          .data("帳號已存在").build();
    }

    if (userService.existsByEmail(req.getEmail())) {
      return ApiResponse.<String>builder()
          .httpCode(HttpStatus.BAD_REQUEST.value()).status("error")
          .data("Email 已存在").build();
    }

    UserInfo userInfo = UserInfo.builder().username(req.getUsername())
        .password(passwordEncoder.encode(req.getPassword()))
        .email(req.getEmail()).fullName(req.getFullName()).phone(req.getPhone())
        .build();

    try {
      userService.save(userInfo);
      return ApiResponse.<String>builder().httpCode(HttpStatus.OK.value())
          .status("success").data("註冊成功").build();

    } catch (Exception e) {
      return ApiResponse.<String>builder()
          .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).status("error")
          .data("註冊失敗：" + e.getMessage()).build();
    }

  }

  public ApiResponse<String> login(LoginRequest req) {
    UserInfo userInfo = userService.findUserByUsername(req.getUsername());

    if (userInfo == null) {
      throw new ApplicationException("使用者不存在");
    }

    if (!passwordEncoder.matches(req.getPassword(), userInfo.getPassword())) {
      throw new ApplicationException("密碼錯誤");
    }

    return ApiResponse.success("登入成功", jwtUtil.generateToken(userInfo));
  }

  public ApiResponse<UserResponse> findUserByUsername(String username) {
    UserInfo userInfo = userService.findUserByUsername(username);

    if (userInfo == null) {
      return ApiResponse.error(username + "使用者不存在", HttpStatus.NOT_FOUND);
    }

    UserResponse userResponse = UserResponse.builder().id(userInfo.getId())
        .username(userInfo.getUsername()).email(userInfo.getEmail())
        .fullName(userInfo.getFullName()).phone(userInfo.getPhone())
        .role(userInfo.getRole()).createdAt(userInfo.getCreatedAt()).build();

    return ApiResponse.<UserResponse>builder().httpCode(HttpStatus.OK.value())
        .status("success").data(userResponse).build();
  }

  public ApiResponse<UserInfo> updateUserProfile(String username,
      UpdateUserRequest request) {
    UserInfo userInfo = userService.findUserByUsername(username);

    if (userInfo == null) {
      return ApiResponse.error("使用者不存在", HttpStatus.NOT_FOUND);
    }

    userInfo.setFullName(request.getFullName());
    userInfo.setPhone(request.getPhone());
    userInfo.setEmail(request.getEmail());

    try {
      UserInfo upUserInfo = userService.save(userInfo);
      return ApiResponse.success(upUserInfo);
    } catch (Exception e) {
      return ApiResponse.error(e.getMessage(), HttpStatus.ACCEPTED);
    }

  }

  public ApiResponse<List<UserResponse>> getCurrentUser() {
    List<UserInfo> users = userService.findAll();
    List<UserResponse> userResponses = users.stream()
        .map(userInfo -> UserResponse.builder().id(userInfo.getId())
            .username(userInfo.getUsername()).email(userInfo.getEmail())
            .fullName(userInfo.getFullName()).phone(userInfo.getPhone())
            .role(userInfo.getRole()).createdAt(userInfo.getCreatedAt())
            .build())
        .toList();
    return ApiResponse.success(userResponses);
  }

}
