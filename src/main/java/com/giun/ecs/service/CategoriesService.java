package com.giun.ecs.service;

import com.giun.ecs.dto.request.AddOptionRequest;
import com.giun.ecs.dto.response.Outbound;

public interface CategoriesService {

    public Outbound addCategorie(AddOptionRequest req) throws Exception;
}
