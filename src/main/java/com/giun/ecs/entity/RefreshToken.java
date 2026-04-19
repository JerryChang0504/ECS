package com.giun.ecs.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshToken extends BaseEntity {

  /**
   * Refresh Token 主鍵。
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 此 Refresh Token 所屬的使用者。
   */
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private UserInfo userInfo;

  /**
   * 發給前端的 Refresh Token 字串。
   */
  @Column(name = "token", nullable = false, unique = true, length = 1000)
  private String token;

  /**
   * Refresh Token 到期時間。
   */
  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  /**
   * 此 Refresh Token 是否已被撤銷。
   */
  @Builder.Default
  @Column(name = "revoked", nullable = false)
  private boolean revoked = false;
}
