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

import com.giun.ecs.dto.request.AddOptionReq;
import com.giun.ecs.dto.request.EditOptionReq;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.service.OptionlistService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/options")
@Tag(name = "options", description = "選項管理API")
public class OptionlistController extends BaseController {

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
        checkRequired(req.getListName(), req.getName(), req.getValue());
        Outbound response = optionlistService.addOptions(req);
        return ResponseEntity.ok(response);
    }

    /**
     * 更新選項
     * 
     * @throws Exception
     */
    @PutMapping("edit/{id}")
    public ResponseEntity<Outbound> editOptions(@PathVariable Integer id, @RequestBody EditOptionReq req)
            throws Exception {
        Outbound response = optionlistService.editOptions(id, req);

        return ResponseEntity.ok(response);
    }

}
