package com.giun.ecs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.giun.ecs.dto.request.AddOptionReq;
import com.giun.ecs.dto.request.EditOptionReq;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.entity.OptionsList;
import com.giun.ecs.repository.OptionslistRepository;

import jakarta.transaction.Transactional;

@Service
public class OptionlistService {
    @Autowired
    OptionslistRepository optionlistrepository;

    /**
     * 選項查詢
     */
    public Outbound getOptions() {
        List<OptionsList> optionlist = optionlistrepository.findAll();
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
        optionlistrepository.save(option);
        return Outbound.ok();
    }

    /**
     * 更新選項
     */
    public Outbound editOptions(Integer id, EditOptionReq req) throws Exception {
        OptionsList option = optionlistrepository.findById(id).orElseThrow(() -> new Exception("選項不存在"));
        option.setListName(req.getListName());
        option.setName(req.getName());
        option.setValue(req.getValue());
        option.setSortOrder(req.getSortOrder());
        option.setIsActive(req.getIsActive());
        option.setDescription(req.getDescription());
        optionlistrepository.save(option);
        return Outbound.ok();
    }
}
