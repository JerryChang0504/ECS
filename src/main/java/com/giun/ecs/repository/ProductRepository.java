package com.giun.ecs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.giun.ecs.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(" SELECT P FROM Product P WHERE P.states > 0")
    List<Product> findByStatesNotDelete();
}
