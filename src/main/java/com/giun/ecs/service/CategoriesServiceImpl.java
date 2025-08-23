package com.giun.ecs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.giun.ecs.dto.request.AddOptionRequest;
import com.giun.ecs.dto.response.OptionResponse;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.entity.Categories;
import com.giun.ecs.repository.CategoriesRepository;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Override
    public Outbound addCategorie(AddOptionRequest req) throws Exception {

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
        List<OptionResponse> result = categoriesRepository.findAll().stream()
                .map(Categories -> {
                    return OptionResponse.builder()
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

}