package com.giun.ecs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.giun.ecs.dto.request.EditOptionRequest;
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
        return Outbound.ok();
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
        return Outbound.ok();
    }

    /**
     * 更新選項
     */
    public Outbound editOption(Integer id, EditOptionRequest req) throws Exception {
        OptionList option = optionRepository.findById(id).orElseThrow(() -> new Exception("選項不存在"));
        option.setListName(req.getListName());
        option.setName(req.getName());
        option.setValue(req.getValue());
        option.setSortOrder(req.getSortOrder());
        option.setIsActive(req.getIsActive());
        option.setDescription(req.getDescription());
        optionRepository.save(option);
        return Outbound.ok();
    }

    /**
     * 關閉選項
     * 
     * @throws Exception
     */
    public Outbound deleteOptions(Integer id) throws Exception {
        OptionList option = optionRepository.findById(id).orElseThrow(() -> new Exception("選項不存在"));
        option.setIsActive(false);
        optionRepository.save(option);
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
        List<OptionList> optionlist = optionRepository.findByListNameAsc(listName);
        return Outbound.ok(optionlist);
    }

}
