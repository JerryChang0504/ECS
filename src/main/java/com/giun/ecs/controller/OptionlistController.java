package com.giun.ecs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giun.ecs.dto.request.AddOptionReq;
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
    public ResponseEntity<Outbound> getOptions() throws Exception {
        Outbound response = optionlistService.getOptions();
        return ResponseEntity.ok(response);
    }

    /**
     * 新增選項
     */
    @PostMapping("/add")
    public ResponseEntity<Outbound> addOptions(@RequestBody AddOptionReq req) throws Exception {
        Outbound response = optionlistService.addOptions(req);
        return ResponseEntity.ok(response);
    }

}
