package com.giun.ecs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "generic_options")
public class Categories extends BaseEntity {
  /**
   * 商品編號
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  /**
   * 商品名稱
   */
  @Column(name = "list_name", nullable = false)
  private String listname;

  /**
   * 商品類別
   */
  @Column(name = "name", nullable = false)
  private String name;

  /**
   * 商品價格
   */
  @Column(name = "value", nullable = false)
  private String value;

  /**
   * 商品庫存
   */
  @Column(name = "sort_order", nullable = false)
  private Integer sortorder;

  /**
   * 商品描述
   */
  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  /**
   * 商品狀態
   */
  @Column(name = "description", nullable = false)
  private String description;
}
