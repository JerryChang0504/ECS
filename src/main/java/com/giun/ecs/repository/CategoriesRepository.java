package com.giun.ecs.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.giun.ecs.entity.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> {


  @Query(nativeQuery = true,
      value = "SELECT * FROM generic_options WHERE list_name = ?1 AND is_active = true  ORDER BY sort_order ASC")
  List<Categories> findByListNameAsc(String listName);

  List<Categories> findByListNameAndIsActiveTrueOrderBySortOrderAsc(String listName);

  @Query(nativeQuery = true,
      value = "SELECT * FROM generic_options WHERE list_name = ?1 AND is_active = true ORDER BY sort_order DESC")
  List<Categories> findByListNameDesc(String listName);

  List<Categories> findByListNameAndIsActiveTrueOrderBySortOrderDesc(String listName);

}
