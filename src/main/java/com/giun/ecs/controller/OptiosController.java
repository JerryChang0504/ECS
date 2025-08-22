package com.giun.ecs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.giun.ecs.dto.request.AddOptionReq;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.exception.ApplicationException;
import com.giun.ecs.service.CategoriesService;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/options")
@Tag(name = "Options", description = "選項管理")
public class OptiosController {

  @Autowired
  private CategoriesService categoriesService;

  /**
   * 取得所有選項
   * 
   * @return
   * @throws Exception
   */
  @GetMapping("/all")
  public ResponseEntity<Outbound> getCategoriesService() throws Exception {
    return ResponseEntity.ok(categoriesService.getAllCategories());
  }

  /**
   * 新增選項
   * 
   * @param req
   * @return
   * @throws Exception
   */
  @PostMapping("/add")
  public ResponseEntity<Outbound> addOption(@RequestBody AddOptionReq req) throws Exception {
    Outbound resp = categoriesService.addCategory(req);
    return ResponseEntity.ok(resp);
  }


  /**
   * 取得選項分類
   * 
   * @param listName
   * @return
   * @throws ApplicationException
   */
  @GetMapping("/list")
  public ResponseEntity<Outbound> getCategoriesByListName(@Param("listName") String listName)
      throws ApplicationException {
    return ResponseEntity.ok(categoriesService.getCategoriesByListName(listName));
  }
}
