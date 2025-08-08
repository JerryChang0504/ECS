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
@Table(name = "product")
public class Product extends BaseEntity {
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
  @Column(name = "name", nullable = false)
  private String name;

  /**
   * 商品類別
   */
  @Column(name = "category", nullable = false)
  private String category;

  /**
   * 商品描述
   */
  @Column(name = "description", nullable = false)
  private String description;

  /**
   * 商品價格
   */
  @Column(name = "price", nullable = false)
  private Integer price;

  /**
   * 圖片資料
   */
  @Lob
  @Column(name = "image_data", columnDefinition = "LONGBLOB")
  private byte[] imageData;

  /**
   * 圖片類型
   */
  @Column(name = "image_type")
  private String imageType;
}
