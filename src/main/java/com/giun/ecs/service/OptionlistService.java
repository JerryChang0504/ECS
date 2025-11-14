package com.giun.ecs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.giun.ecs.dto.request.AddOptionReq;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.entity.OptionsList;
import com.giun.ecs.repository.OptionlistRepository;

import jakarta.transaction.Transactional;

@Service
public class OptionlistService {

    @Autowired
    private OptionlistRepository optionlistRepository;

    /**
     * 選項查詢
     */
    public Outbound getOptions() {
        List<OptionsList> optionlist = optionlistRepository.findAll();
        return Outbound.ok(optionlist);
    }

    /**
     * 新增選項
     * 
     * @param req
     */
    @Transactional
    public Outbound addOptions(AddOptionReq req) throws Exception {
        OptionsList option = OptionsList.builder()
                .listName(req.getListName())
                .name(req.getName())
                .value(req.getValue())
                .sortOrder(req.getSortOrder())
                .isActive(req.getIsActive())
                .description(req.getDescription())
                .build();
        optionlistRepository.save(option);
        return Outbound.ok();
    }
}