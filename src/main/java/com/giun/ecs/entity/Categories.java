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

/**
 * CREATE TABLE generic_options ( id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主鍵', list_name
 * VARCHAR(100) NOT NULL COMMENT '列表名稱，例如: category, order_status', name VARCHAR(255) NOT NULL
 * COMMENT '選項顯示名稱，用於el-option的label', value VARCHAR(255) NOT NULL COMMENT
 * '選項的唯一值，用於el-option的value', sort_order INT DEFAULT 0 COMMENT '排序順序', is_active BOOLEAN DEFAULT
 * TRUE COMMENT '是否啟用', description TEXT COMMENT '選項描述', created_at TIMESTAMP DEFAULT
 * CURRENT_TIMESTAMP COMMENT '創建時間', updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE
 * CURRENT_TIMESTAMP );
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "generic_options")
public class Categories extends BaseEntity {

  /**
   * id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  /**
   * 選項分類
   */
  @Column(name = "list_name", nullable = false)
  private String listName;

  /**
   * 選項顯示名稱
   */
  @Column(name = "name", nullable = false)
  private String name;


  /**
   * 選項的唯一值
   */
  @Column(name = "value", nullable = false)
  private String value;

  /**
   * 排序順序
   */
  @Column(name = "sort_order")
  private Integer sortOrder;

  /**
   * 是否啟用
   */
  @Column(name = "is_active")
  private Boolean isActive;


  /**
   * 選項描述
   */
  @Column(name = "description")
  private String description;

}
