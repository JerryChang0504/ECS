package com.giun.ecs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.giun.ecs.entity.OptionsList;

@Repository
public interface OptionlistRepository extends JpaRepository<OptionsList, Integer> {

}
