package com.giun.ecs.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.giun.ecs.dto.request.AddOptionReq;
import com.giun.ecs.dto.response.OptionResp;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.dto.response.SelectOptions;
import com.giun.ecs.entity.Categories;
import com.giun.ecs.repository.CategoriesRepository;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Override
    public Outbound addCategorie(AddOptionReq req) throws Exception {

        Categories categories = Categories.builder()
                .listName(req.getListName())
                .name(req.getName())
                .value(req.getValue())
                .sortOrder(req.getSortOrder())
                .isActive(req.getIsActive())
                .description(req.getDescription())
                .build();
        try {
            categoriesRepository.save(categories);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Outbound.ok("Category added successfully");
    }

    @Override
    public Outbound allCategories() throws Exception {
        List<OptionResp> result = categoriesRepository.findAll().stream()
                // 查詢自動排序
                .sorted(Comparator.comparing(Categories::getListName)
                        .thenComparing(Categories::getSortOrder))

                .map(Categories -> {
                    return OptionResp.builder()
                            .id(Categories.getId())
                            .listName(Categories.getListName())
                            .key(Categories.getName())
                            .value(Categories.getValue())
                            .sortOrder(Categories.getSortOrder())
                            .isActive(Categories.getIsActive())
                            .description(Categories.getDescription())
                            .build();
                }).collect(Collectors.toList());

        return Outbound.ok(result);
    }

    @Override
    public Outbound deleteCategorie(Integer id) throws Exception {
        categoriesRepository.deleteById(id);
        return Outbound.ok("Category deleted successfully");
    }

    @Override
    public Outbound updateCategorie(Integer id, AddOptionReq req) throws Exception {
        Categories categorie = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Categories updateCategories = Categories.builder()
                .id(categorie.getId())
                .listName(req.getListName())
                .name(req.getName())
                .value(req.getValue())
                .sortOrder(req.getSortOrder())
                .isActive(req.getIsActive())
                .description(req.getDescription())
                .build();

        categoriesRepository.save(updateCategories);

        return Outbound.ok("Category updated successfully");
    }

    @Override
    public Outbound getCategoriesByListName(String listName) throws Exception {
        List<Categories> categories = categoriesRepository.findByListNameAndIsActiveTrueOrderBySortOrderAsc(listName);

        if (categories.isEmpty()) {
            throw new RuntimeException("Categories not found");
        }

        List<SelectOptions> result = categories.stream()
                .map(categorie -> {
                    return SelectOptions.builder()
                            .label(categorie.getName())
                            .value(categorie.getValue())
                            .sortOrder(categorie.getSortOrder())
                            .build();
                }).collect(Collectors.toList());

        return Outbound.ok(result);
    }

}
