package com.giun.ecs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.giun.ecs.dto.request.AddOptionReq;
import com.giun.ecs.dto.response.CategoriesResponse;
import com.giun.ecs.dto.response.OptionResp;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.entity.Categories;
import com.giun.ecs.exception.ApplicationException;
import com.giun.ecs.repository.CategoriesRepository;

public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    /**
     * 取得所有選項
     */
    @Override
    public Outbound getAllCategories() {

        List<CategoriesResponse> result = categoriesRepository.findAll().stream()
                .map(category -> {

                    return CategoriesResponse.builder()
                            .id(category.getId())
                            .listName(category.getListName())
                            .lable(category.getName())
                            .value(category.getValue())
                            .sortOrder(category.getSortOrder())
                            .isActive(category.getIsActive())
                            .description(category.getDescription())
                            .build();
                }).collect(Collectors.toList());

        return Outbound.ok(result);
    }

    /**
     * 新增選項
     */
    @Override
    public Outbound addCategory(AddOptionReq req) {

        Categories categorie = Categories.builder()
                .listname(req.getListName())
                .name(req.getOptionName())
                .value(req.getOptionValue())
                .sortOrder(req.getSortOrder())
                .isActive(req.getIsActive())
                .description(req.getDescription())
                .build();

        Categories addCategorie = categoriesRepository.save(categorie);

        return Outbound.ok(addCategorie);
    }

    /**
     * 取得選項分類
     */
    @Override
    public Outbound getCategoriesByListName(String listName) throws ApplicationException {
        List<Categories> categories = categoriesRepository.findByListNameAndIsActiveTrueOrderBySortOrderAsc(listName);

        if (categories.isEmpty()) {
            throw new ApplicationException("Categories not found");
        }

        List<OptionResp> result = categories.stream()
                .map(category -> {
                    return OptionResp.builder()
                            .label(category.getName())
                            .value(category.getValue())
                            .sortOrder(category.getSortOrder())
                            .build();
                }).collect(Collectors.toList());

        return Outbound.ok(result);
    }

}
