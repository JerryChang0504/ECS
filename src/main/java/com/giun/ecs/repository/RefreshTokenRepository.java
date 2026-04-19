package com.giun.ecs.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.giun.ecs.entity.RefreshToken;
import com.giun.ecs.entity.UserInfo;

/**
 * Refresh Token 儲存庫
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  /**
   * 根據 token 查找有效的 refresh token
   * 
   * @param token refresh token
   * @return 有效的 refresh token
   */
  Optional<RefreshToken> findByTokenAndRevokedFalse(String token);

  /**
   * 根據 userInfo 查找有效的 refresh token
   * 
   * @param userInfo 使用者資訊
   * @return 有效的 refresh token
   */
  List<RefreshToken> findAllByUserInfoAndRevokedFalse(UserInfo userInfo);
}
