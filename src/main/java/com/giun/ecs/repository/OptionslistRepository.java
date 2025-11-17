package com.giun.ecs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.giun.ecs.entity.OptionsList;

@Repository
public interface OptionslistRepository extends JpaRepository<OptionsList, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM Optionslist WHERE list_name = ?1 AND is_active = true  ORDER BY sort_order ASC")
    List<OptionsList> findByListNameAsc(String listName);

    List<OptionsList> findByListNameAndIsActiveTrueOrderBySortOrderAsc(String listName);

    @Query(nativeQuery = true, value = "SELECT * FROM Optionslist WHERE list_name = ?1 AND is_active = true ORDER BY sort_order DESC")
    List<OptionsList> findByListNameDesc(String listName);

    List<OptionsList> findByListNameAndIsActiveTrueOrderBySortOrderDesc(String listName);
}
