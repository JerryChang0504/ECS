package com.giun.ecs.service;

import com.giun.ecs.dto.request.AddOptionReq;
import com.giun.ecs.dto.response.Outbound;

public interface CategoriesService {

    public Outbound addCategorie(AddOptionReq req) throws Exception;

    public Outbound allCategories() throws Exception;

    public Outbound updateCategorie(Integer id, AddOptionReq req) throws Exception;

    public Outbound deleteCategorie(Integer id) throws Exception;
}
