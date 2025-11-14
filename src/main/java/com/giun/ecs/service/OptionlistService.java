package com.giun.ecs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.entity.OptionsList;
import com.giun.ecs.repository.OptionslistRepository;

@Service
public class OptionlistService {
    @Autowired
    OptionslistRepository optionlistrepository;

    public Outbound getOptions() {
        List<OptionsList> optionlist = optionlistrepository.findAll();
        return Outbound.ok(optionlist);
    }
}
