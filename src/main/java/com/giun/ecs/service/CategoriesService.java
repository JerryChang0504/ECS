package com.giun.ecs.service;

import com.giun.ecs.dto.request.AddOptionReq;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.exception.ApplicationException;

/**
 * 選項分類服務
 */
public interface CategoriesService {

  /**
   * 選項所有選項
   * 
   * @return
   */
  public Outbound getAllCategories();

  /**
   * 選項分類新增
   * 
   * @param req
   * @return
   */
  public Outbound addCategory(AddOptionReq req);

  /**
   * 選項分類取得
   * 
   * @param listName
   * @return
   * @throws ApplicationException
   */
  public Outbound getCategoriesByListName(String listName) throws ApplicationException;

}
