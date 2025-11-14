package com.giun.ecs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.giun.ecs.entity.OptionList;

public interface OptionRepository extends JpaRepository<OptionList, Integer> {

}
