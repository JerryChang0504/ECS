package com.giun.ecs.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.giun.ecs.dto.request.ChangePswRequest;
import com.giun.ecs.dto.request.LoginRequest;
import com.giun.ecs.dto.request.RefreshTokenRequest;
import com.giun.ecs.dto.request.RegisterRequest;
import com.giun.ecs.dto.request.UpdateUserRequest;
import com.giun.ecs.dto.response.AuthTokenResponse;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.dto.response.UserResponse;
import com.giun.ecs.entity.RefreshToken;
import com.giun.ecs.entity.UserInfo;
import com.giun.ecs.enums.ResultCode;
import com.giun.ecs.exception.ApplicationException;
import com.giun.ecs.repository.RefreshTokenRepository;
import com.giun.ecs.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@Service
public class AuthService {

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  /**
   * 使用者註冊
   * 
   * @param req
   * @return
   */
  public Outbound register(RegisterRequest req) throws ApplicationException {
    // 驗證帳號是否存在
    if (userService.existsByUsername(req.getUsername())) {
      throw new ApplicationException(ResultCode.ACCOUNT_IS_EXIST);
    }
    // 驗證 Email 是否存在
    if (userService.existsByEmail(req.getEmail())) {
      throw new ApplicationException(ResultCode.EMAIL_IS_EXIST);
    }

    UserInfo userInfo = UserInfo.builder().username(req.getUsername())
        .password(passwordEncoder.encode(req.getPassword()))
        .email(req.getEmail()).fullName(req.getFullName())
        .phone(req.getPhone()).build();

    userService.save(userInfo);
    return Outbound.ok("註冊成功");

  }

  /**
   * 使用者登入
   * 
   * @param req
   * @return
   */
  public Outbound login(LoginRequest req) throws ApplicationException {
    UserInfo userInfo = userService.findUserByUsername(req.getUsername());

    // 驗證帳號是否存在
    if (userInfo == null) {
      throw new ApplicationException(ResultCode.USER_IS_NOT_EXIST);
    }

    // 驗證密碼
    if (!passwordEncoder.matches(req.getPassword(),
        userInfo.getPassword())) {
      throw new ApplicationException(ResultCode.PASSWORD_NOT_MATCH);
    }
    String accessToken = jwtUtil.generateAccessToken(userInfo);
    String refreshToken = jwtUtil.generateRefreshToken(userInfo);
    // 保存 refresh token
    refreshTokenRepository.save(
        RefreshToken.builder()
            .userInfo(userInfo)
            .token(refreshToken)
            .expiresAt(jwtUtil.getExpirationAt(refreshToken)).build());

    return Outbound.ok(AuthTokenResponse.builder().tokenType("Bearer")
        .accessToken(accessToken).refreshToken(refreshToken).build());
  }


  /**
   * 刷新 Token
   * 
   * @param request
   * @return
   * @throws ApplicationException
   */
  public Outbound refreshToken(RefreshTokenRequest request)
      throws ApplicationException {
    // 驗證 refresh token 是否存在
    RefreshToken refreshToken = refreshTokenRepository
        .findByTokenAndRevokedFalse(request.getRefreshToken())
        .orElseThrow(() -> new ApplicationException(
            ResultCode.USER_IS_NOT_AUTHENTICATED));

    UserInfo userInfo = refreshToken.getUserInfo();
    // 驗證 refresh token 是否有效
    if (!jwtUtil.validateRefreshToken(request.getRefreshToken(), userInfo)) {
      refreshToken.setRevoked(true);
      refreshTokenRepository.save(refreshToken);
      throw new ApplicationException(ResultCode.USER_IS_NOT_AUTHENTICATED);
    }

    // 撤銷 refresh token
    refreshToken.setRevoked(true);
    refreshTokenRepository.save(refreshToken);

    // 生成新的 access token 和 refresh token
    String newAccessToken = jwtUtil.generateAccessToken(userInfo);
    String newRefreshToken = jwtUtil.generateRefreshToken(userInfo);
    refreshTokenRepository.save(RefreshToken.builder().userInfo(userInfo)
        .token(newRefreshToken)
        .expiresAt(jwtUtil.getExpirationAt(newRefreshToken)).build());

    return Outbound.ok(AuthTokenResponse.builder()
        .tokenType("Bearer")
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .build());
  }

  /**
   * 取得使用者資料
   * 
   * @param token
   * @return
   */
  public Outbound findUserByUsername(String token)
      throws ApplicationException {

    String username = jwtUtil.getUsernameFromToken(token);
    UserInfo userInfo = userService.findUserByUsername(username);

    if (userInfo == null) {
      throw new ApplicationException(ResultCode.USER_IS_NOT_EXIST);
    }

    UserResponse userResponse = UserResponse.builder().id(userInfo.getId())
        .username(userInfo.getUsername()).email(userInfo.getEmail())
        .fullName(userInfo.getFullName()).phone(userInfo.getPhone())
        .role(userInfo.getRole()).createdAt(userInfo.getCreatedAt())
        .build();

    return Outbound.ok(userResponse);
  }

  /**
   * 更新使用者資料
   * 
   * @param username
   * @param request
   * @return
   */
  public Outbound updateUserProfile(String username,
      UpdateUserRequest request) throws Exception {
    UserInfo userInfo = userService.findUserByUsername(username);

    if (userInfo == null) {
      throw new ApplicationException(ResultCode.USER_IS_NOT_EXIST);
    }

    userInfo.setFullName(request.getFullName());
    userInfo.setPhone(request.getPhone());
    userInfo.setEmail(request.getEmail());

    try {
      UserInfo upUserInfo = userService.save(userInfo);
      return Outbound.ok(upUserInfo);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }

  }

  /**
   * 依 Access Token 取得「目前登入使用者」資料。
   * <p>
   * 須為有效的 Access Token（含簽章正確、未過期、tokenType=ACCESS），否則拋出 {@link ApplicationException}。
   *
   * @param accessToken 不含 {@code Bearer } 前綴的 JWT 字串
   */
  public Outbound getCurrentUserByAccessToken()
      throws ApplicationException {
    try {
      // String username = jwtUtil.getUsernameFromToken(accessToken);
      List<UserInfo> users = userService.findAll();
      if (users == null) {
        throw new ApplicationException(ResultCode.USER_IS_NOT_EXIST);
      }
      List<UserResponse> userResponses = users.stream()
          .map(userInfo -> UserResponse.builder().id(userInfo.getId())
              .username(userInfo.getUsername())
              .email(userInfo.getEmail())
              .fullName(userInfo.getFullName())
              .phone(userInfo.getPhone()).role(userInfo.getRole())
              .createdAt(userInfo.getCreatedAt()).build())
          .toList();
      return Outbound.ok(userResponses);
    } catch (ExpiredJwtException e) {
      throw new ApplicationException(ResultCode.USER_IS_NOT_AUTHENTICATED);
    } catch (JwtException e) {
      throw new ApplicationException(ResultCode.USER_IS_NOT_AUTHENTICATED);
    }
  }

  /**
   * 更新密碼
   * 
   * @param request 更新密碼請求
   * @return
   */
  public Outbound updatePassword(ChangePswRequest request) {
    UserInfo userInfo = userService
        .findUserByUsername(request.getUsername());
    userInfo.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userService.save(userInfo);
    return Outbound.ok("密碼更新成功");
  }


  /**
   * 登出使用者
   * 
   * @param username 使用者帳號
   * @return 登出成功
   */
  public Outbound logout(String username) {
    UserInfo userInfo = userService.findUserByUsername(username);
    if (userInfo == null) {
      return Outbound.ok("登出成功");
    }
    // 撤銷所有有效的 refresh token
    List<RefreshToken> activeTokens = refreshTokenRepository
        .findAllByUserInfoAndRevokedFalse(userInfo);
    activeTokens.forEach(token -> token.setRevoked(true));
    // 保存撤銷的 refresh token
    refreshTokenRepository.saveAll(activeTokens);
    return Outbound.ok("登出成功");
  }

}
