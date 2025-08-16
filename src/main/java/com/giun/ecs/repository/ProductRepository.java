package com.giun.ecs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.giun.ecs.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "UPDATE product SET states = :states WHERE id = :id")
    void updateProductStatus(Integer id, String states);

}
