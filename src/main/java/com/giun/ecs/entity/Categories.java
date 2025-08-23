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
@Table(name = "generic_options")
public class Categories extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "list_name", nullable = false)
    private String listName;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "description")
    private String description;

}
