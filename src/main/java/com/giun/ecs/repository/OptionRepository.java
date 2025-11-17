package com.giun.ecs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.giun.ecs.entity.OptionList;

public interface OptionRepository extends JpaRepository<OptionList, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM Optionlist WHERE list_name = ?1 AND is_active = true  ORDER BY sort_order ASC")
    List<OptionList> findByListNameAsc(String listName);

    List<OptionList> findByListNameAndIsActiveTrueOrderBySortOrderAsc(String listName);

    @Query(nativeQuery = true, value = "SELECT * FROM Optionlist WHERE list_name = ?1 AND is_active = true ORDER BY sort_order DESC")
    List<OptionList> findByListNameDesc(String listName);

    List<OptionList> findByListNameAndIsActiveTrueOrderBySortOrderDesc(String listName);
}
