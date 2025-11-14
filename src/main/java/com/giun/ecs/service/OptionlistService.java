package com.giun.ecs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.entity.OptionsList;
import com.giun.ecs.repository.OptionlistRepository;

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
}
