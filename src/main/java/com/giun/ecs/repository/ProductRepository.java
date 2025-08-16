package com.giun.ecs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.giun.ecs.entity.Product;

import jakarta.transaction.Transactional;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	@Modifying
	@Transactional
	@Query("UPDATE Product p SET p.states = :states WHERE p.id = :id")
	void updateProductStates(@Param("id") Integer id,
			@Param("states") String states);

}