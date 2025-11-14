package com.giun.ecs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.entity.OptionList;
import com.giun.ecs.repository.OptionRepository;

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
}
