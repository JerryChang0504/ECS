package com.giun.ecs.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.giun.ecs.entity.UserInfo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secretKey;

	// JWT 的有效時間（毫秒）- 這裡設定為 1 小時
	private static final long EXPIRATION_MS = 3600_000;

	/**
	 * 產生 JWT token，內含 username（主體）與自定義的 role 欄位
	 *
	 * @param userInfo
	 *            用戶資訊
	 * @return JWT 字串
	 */
	public String generateToken(UserInfo userInfo) {
		return Jwts.builder().setSubject(userInfo.getUsername()) // 設定主要身份資訊（username）
				.claim("role", userInfo.getRole()) // 加入自定義資訊（例如角色）
				.setIssuedAt(new Date()) // 簽發時間
				.setExpiration(
						new Date(System.currentTimeMillis() + EXPIRATION_MS)) // 過期時間
				.signWith(
						Keys.hmacShaKeyFor(
								secretKey.getBytes(StandardCharsets.UTF_8)),
						SignatureAlgorithm.HS256) // 使用 HMAC SHA256 簽章
				.compact(); // 建立 JWT 字串
	}

	/**
	 * 從 token 中解析出使用者名稱（主體）
	 *
	 * @param token
	 *            JWT 字串
	 * @return username
	 */
	public String getUsernameFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(
						secretKey.getBytes(StandardCharsets.UTF_8)))
				.build().parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * 驗證 JWT 是否有效（簽名正確 & 未過期 & 與當前使用者比對）
	 *
	 * @param token
	 *            JWT 字串
	 * @param userInfo
	 *            驗證用使用者物件
	 * @return 是否有效
	 */
	public boolean validateToken(String token, UserInfo userInfo) {
		String username = getUsernameFromToken(token);
		return username.equals(userInfo.getUsername())
				&& !isTokenExpired(token);
	}

	/**
	 * 判斷 Token 是否已過期
	 *
	 * @param token
	 *            JWT 字串
	 * @return 是否過期
	 */
	private boolean isTokenExpired(String token) {
		Date expiration = Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(
						secretKey.getBytes(StandardCharsets.UTF_8)))
				.build().parseClaimsJws(token).getBody().getExpiration();

		return expiration.before(new Date());
	}
}
