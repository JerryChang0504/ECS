package com.giun.ecs.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.giun.ecs.dto.request.AddOptionReq;
import com.giun.ecs.dto.request.EditOptionReq;
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
        List<OptionsList> optionlist = optionlistRepository.findAll().stream()
                .sorted(Comparator.comparing(OptionsList::getListName)
                        .thenComparing(OptionsList::getSortOrder))
                .map(option -> option).collect(Collectors.toList());
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

    /**
     * 更新選項
     */
    public Outbound editOptions(Integer id, EditOptionReq req) throws Exception {
        OptionsList option = optionlistRepository.findById(id).orElseThrow(() -> new Exception("選項不存在"));
        option.setListName(req.getListName());
        option.setName(req.getName());
        option.setValue(req.getValue());
        option.setSortOrder(req.getSortOrder());
        option.setIsActive(req.getIsActive());
        option.setDescription(req.getDescription());
        optionlistRepository.save(option);
        return Outbound.ok();
    }

    /**
     * 關閉選項
     * 
     * @throws Exception
     */
    public Outbound deleteOptions(Integer id) throws Exception {
        OptionsList option = optionlistRepository.findById(id).orElseThrow(() -> new Exception("選項不存在"));
        option.setIsActive(false);
        optionlistRepository.save(option);
        return Outbound.ok();
    }

    /**
     * 取得選項
     * 
     * @param listName
     * @return
     * @throws Exception
     */
    public Outbound getOptionsByListName(String listName) throws Exception {
        List<OptionsList> optionlist = optionlistRepository.findByListNameAsc(listName);
        return Outbound.ok(optionlist);
    }
}