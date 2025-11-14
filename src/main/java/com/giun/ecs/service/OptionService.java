package com.giun.ecs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.giun.ecs.dto.request.OptionsRequest;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.entity.OptionList;
import com.giun.ecs.repository.OptionRepository;

import jakarta.transaction.Transactional;

@Service
public class OptionService {
    @Autowired
    private OptionRepository optionRepository;

    /**
     * 選項查詢
     */
    public Outbound getOptions() {
        List<OptionList> optionlist = optionRepository.findAll();
        return Outbound.ok(optionlist);
    }

    /**
     * 新增選項
     * 
     * @param req
     * @return
     */
    @Transactional
    public Outbound addOption(OptionsRequest req) {
        OptionList optionList = OptionList.builder()
                .listName(req.getListName())
                .name(req.getName())
                .value(req.getValue())
                .sortOrder(req.getSortOrder())
                .isActive(req.getIsActive())
                .description(req.getDescription())
                .build();
        optionRepository.save(optionList);
        return Outbound.ok(null);
    }
}
