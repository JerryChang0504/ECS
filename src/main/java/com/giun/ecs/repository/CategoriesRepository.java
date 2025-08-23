package com.giun.ecs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.giun.ecs.entity.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> {

}
