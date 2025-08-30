package com.giun.ecs.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.giun.ecs.dto.request.AddOptionReq;
import com.giun.ecs.dto.response.OptionResp;
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
        Categories categories = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Categories updateCategories = Categories.builder()
                .id(categories.getId())
                .listName(req.getListName())
                .name(req.getName())
                .value(req.getValue())
                .sortOrder(req.getSortOrder())
                .isActive(req.getIsActive())
                .description(req.getDescription())
                .build();

        categoriesRepository.save(updateCategories);
        throw new UnsupportedOperationException("Unimplemented method 'updateCategorie'");
    }

}
