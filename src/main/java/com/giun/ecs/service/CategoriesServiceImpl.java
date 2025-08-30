package com.giun.ecs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.giun.ecs.dto.response.OptionResp;
import com.giun.ecs.dto.request.AddOptionReq;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.entity.Categories;
import com.giun.ecs.repository.CategoriesRepository;

@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Override
    public Outbound addCategorie(AddOptionReq req) throws Exception {

        Categories categories = Categories.builder()
                .listname(req.getListName())
                .name(req.getName())
                .value(req.getValue())
                .sortorder(req.getSortOrder())
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
                .map(Categories -> {
                    return OptionResp.builder()
                            .id(Categories.getId())
                            .listName(Categories.getListname())
                            .key(Categories.getName())
                            .value(Categories.getValue())
                            .sortOrder(Categories.getSortorder())
                            .isActive(Categories.getIsActive())
                            .description(Categories.getDescription())
                            .build();
                }).collect(Collectors.toList());

        return Outbound.ok(result);
    }

    @Override
    public Outbound updateCategorie(Integer id, AddOptionReq req) throws Exception {
        Categories categorie = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Categories updateCategories = Categories.builder()
                .id(categorie.getId())
                .listname(req.getListName())
                .name(req.getName())
                .value(req.getValue())
                .sortorder(req.getSortOrder())
                .isActive(req.getIsActive())
                .description(req.getDescription())
                .build();

        categoriesRepository.save(updateCategories);

        return Outbound.ok("Category updated successfully");
    }

}
