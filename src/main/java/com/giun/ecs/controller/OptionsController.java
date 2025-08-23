package com.giun.ecs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giun.ecs.dto.request.AddOptionReq;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.service.CategoriesService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/options")
@Tag(name = "Options", description = "選單管理")
public class OptionsController {

    @Autowired
    private CategoriesService categoriesService;

    @PostMapping("/add")
    public ResponseEntity<Outbound> AddOption(@RequestBody AddOptionReq req) throws Exception {
        Outbound resp = categoriesService.addCategorie(req);
        return ResponseEntity.ok(resp);
    }

}
