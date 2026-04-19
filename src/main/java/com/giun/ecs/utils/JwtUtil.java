package com.giun.ecs.utils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.giun.ecs.entity.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
  private static final String CLAIM_ROLE = "role";
  private static final String CLAIM_TOKEN_TYPE = "tokenType";
  private static final String TOKEN_TYPE_ACCESS = "ACCESS";
  private static final String TOKEN_TYPE_REFRESH = "REFRESH";

  // JWT 祕鑰
  @Value("${jwt.secret}")
  private String secretKey;

  // Access Token 過期時間，預設 1 小時ㄋ
  @Value("${jwt.access-expiration-ms:3600000}")
  private long accessExpirationMs;

  // Refresh Token 過期時間，預設 7 天
  @Value("${jwt.refresh-expiration-ms:604800000}")
  private long refreshExpirationMs;

  /**
   * 產生 JWT token，內含 username（主體）與自定義的 role 欄位
   *
   * @param userInfo 用戶資訊
   * @return JWT 字串
   */
  public String generateAccessToken(UserInfo userInfo) {
    return Jwts.builder()
        .setSubject(userInfo.getUsername()) // 設定主要身份資訊（username）
        .claim(CLAIM_ROLE, userInfo.getRole()) // 加入自定義資訊（例如角色）
        .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS) // Access Token 類型
        .setIssuedAt(new Date()) // 簽發時間
        .setExpiration(
            new Date(System.currentTimeMillis() + accessExpirationMs)) // 過期時間
        .signWith(
            Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)),
            SignatureAlgorithm.HS256) // 使用 HMAC SHA256 簽章
        .compact(); // 建立 JWT 字串
  }

  public String generateRefreshToken(UserInfo userInfo) {
    return Jwts.builder()
        .setSubject(userInfo.getUsername())
        .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_REFRESH)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
        .signWith(
            Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)),
            SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * 從 token 中解析出使用者名稱（主體）
   *
   * @param token JWT 字串
   * @return username
   */
  public String getUsernameFromToken(String token) {
    return parseAllClaims(token).getSubject();
  }

  public String getRoleFromToken(String token) {
    Object role = parseAllClaims(token).get(CLAIM_ROLE);
    return role == null ? null : role.toString();
  }

  /**
   * 驗證 JWT 是否有效（簽名正確 & 未過期 & 與當前使用者比對）
   *
   * @param token JWT 字串
   * @param userInfo 驗證用使用者物件
   * @return 是否有效
   */
  public boolean validateToken(String token, UserInfo userInfo) {
    String username = getUsernameFromToken(token);
    return username.equals(userInfo.getUsername())
        && isAccessToken(token)
        && !isTokenExpired(token);
  }

  /**
   * 驗證 Refresh Token 是否有效
   * 
   * @param token JWT 字串
   * @param userInfo 驗證用使用者物件
   * @return 是否有效
   */
  public boolean validateRefreshToken(String token, UserInfo userInfo) {
    String username = getUsernameFromToken(token);
    return username.equals(userInfo.getUsername())
        && isRefreshToken(token)
        && !isTokenExpired(token);
  }

  /**
   * 判斷 Token 是否為 Access Token
   * 
   * @param token JWT 字串
   * @return 是否為 Access Token
   */
  public boolean isAccessToken(String token) {
    Object tokenType = parseAllClaims(token).get(CLAIM_TOKEN_TYPE);
    return TOKEN_TYPE_ACCESS.equals(tokenType);
  }

  /**
   * 判斷 Token 是否為 Refresh Token
   * 
   * @param token JWT 字串
   * @return 是否為 Refresh Token
   */
  public boolean isRefreshToken(String token) {
    Object tokenType = parseAllClaims(token).get(CLAIM_TOKEN_TYPE);
    return TOKEN_TYPE_REFRESH.equals(tokenType);
  }

  /**
   * 取得 Token 過期時間
   * 
   * @param token
   * @return
   */
  public LocalDateTime getExpirationAt(String token) {
    Date expiration = parseAllClaims(token).getExpiration();
    return expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  /**
   * 判斷 Token 是否已過期
   *
   * @param token JWT 字串
   * @return 是否過期
   */
  private boolean isTokenExpired(String token) {
    Date expiration = parseAllClaims(token).getExpiration();
    return expiration.before(new Date());
  }

  /**
   * 解析所有 claims
   * 
   * @param token JWT 字串
   * @return 所有 claims
   */
  private Claims parseAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
        .build().parseClaimsJws(token).getBody();
  }
}
