package com.giun.ecs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "users")
public class UserInfo extends BaseEntity {

  /**
   * 使用者 ID
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 使用者帳號
   */
  @Column(name = "username", unique = true, nullable = false)
  private String username;

  /**
   * 使用者密碼
   */
  @Column(name = "password", nullable = false)
  private String password;

  /**
   * 使用者電子郵件
   */
  @Column(name = "email", unique = true, nullable = false)
  private String email;

  /**
   * 使用者姓名
   */
  @Column(name = "full_name")
  private String fullName;

  /**
   * 使用者電話
   */
  @Column(name = "phone")
  private String phone;

  /**
   * 使用者角色
   */
  @Builder.Default // 預設值
  private String role = "USER";

  /**
   * 使用者狀態
   */
  @Builder.Default // 預設值
  private Integer status = 1;

}
