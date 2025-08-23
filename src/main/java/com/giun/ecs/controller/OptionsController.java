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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/list")
    public ResponseEntity<Outbound> allOptions() throws Exception {
        Outbound resp = categoriesService.allCategories();
        return ResponseEntity.ok(resp);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<Outbound> deleteOption(@PathVariable("id") Integer id) throws Exception {
        Outbound resp = categoriesService.deleteCategorie(id);
        return ResponseEntity.ok(resp);
    }

}
