package com.giun.ecs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giun.ecs.dto.request.EditOptionRequest;
import com.giun.ecs.dto.request.OptionsRequest;
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

    @PostMapping("/add")
    public ResponseEntity<Outbound> addOption(@RequestBody OptionsRequest req) {
        Outbound response = optionService.addOption(req);

        return ResponseEntity.ok(response);
    }

    /**
     * 更新選項
     */
    @PutMapping("edit/{id}")
    public ResponseEntity<Outbound> editOption(@PathVariable Integer id, @RequestBody EditOptionRequest req)
            throws Exception {
        Outbound response = optionService.editOption(id, req);

        return ResponseEntity.ok(response);
    }

}
