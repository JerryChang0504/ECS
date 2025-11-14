package com.giun.ecs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.service.OptionService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/options")
@Tag(name = "Options", description = "選項管理API")
public class OptionController {
    @Autowired
    private OptionService optionService;

    /**
     * 查詢選項
     */
    @GetMapping("/list")
    public ResponseEntity<Outbound> getOptions() {
        Outbound reponse = optionService.getOptions();
        return ResponseEntity.ok(reponse);
    }
}
