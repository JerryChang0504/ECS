package com.giun.ecs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.service.OptionlistService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/options")
@Tag(name = "options", description = "選項管理API")
public class OptionlistController {

    @Autowired
    private OptionlistService optionlistService;

    /**
     * 選項查詢
     */
    @GetMapping("/list")
    public ResponseEntity<Outbound> getOptions() {
        Outbound response = optionlistService.getOptions();
        return ResponseEntity.ok(response);
    }

    // @PostMapping("/add")
    // public String addOptions(@RequestBody String entity) {
    // // TODO: process POST request

    // return entity;
    // }

}