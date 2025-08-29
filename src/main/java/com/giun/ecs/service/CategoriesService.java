package com.giun.ecs.service;

import com.giun.ecs.dto.request.OptionReq;
import com.giun.ecs.dto.response.Outbound;

public interface CategoriesService {

  public Outbound addCategorie(OptionReq req) throws Exception;

  public Outbound allCategories() throws Exception;

  public Outbound updateCategorie(Integer id, OptionReq req) throws Exception;

  public Outbound deleteCategorie(Integer id) throws Exception;

  public Outbound getCategoriesByListName(String listName) throws Exception;

}
