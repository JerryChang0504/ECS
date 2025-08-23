package com.giun.ecs.service;

import com.giun.ecs.dto.request.OptionReq;
import com.giun.ecs.dto.response.Outbound;

public interface CategoriesService {

    public Outbound addCategorie(OptionReq req) throws Exception;
}
