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

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Optionslist")
public class OptionsList {
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