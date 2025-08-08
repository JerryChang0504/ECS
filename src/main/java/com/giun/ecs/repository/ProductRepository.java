package com.giun.ecs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.giun.ecs.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
