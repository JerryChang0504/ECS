package com.giun.ecs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.giun.ecs.dto.request.ProductUploadReq;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.entity.Product;
import com.giun.ecs.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Product", description = "產品相關 API")
public class ProductController {

  @Autowired
  private ProductService productService;

  /**
   * 新增產品
   * 
   * @param req 產品資料
   * @return
   */
  @PostMapping(path = "/addProducts", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Product> uploadProduct(
      @RequestBody ProductUploadReq req) {
    Product saved = productService.saveProduct(req);
    return ResponseEntity.ok(saved);
  }

  /**
   * 取得所有產品資料
   * 
   * @return
   */
  @GetMapping("/products")
  public ResponseEntity<Outbound> getProducts() {
    Outbound response = productService.getAllProducts();
    return ResponseEntity.ok(response);
  }

  /**
   * 取得產品資料
   * 
   * @param id 商品ID
   * @return
   */
  @GetMapping("/products/edit/{id}")
  public ResponseEntity<Outbound> getProductById(@PathVariable("id") Integer id) {
    Outbound response = productService.getProductById(id);
    return ResponseEntity.ok(response);
  }

  /**
   * 更新產品
   * 
   * @param id  商品ID
   * @param req 更新資料
   * @return
   */
  @PutMapping("/updateProducts/{id}")
  public ResponseEntity<Outbound> updateProduct(@PathVariable("id") Integer id,
      @RequestBody ProductUploadReq req) {
    Outbound response = productService.updateProduct(id, req);
    return ResponseEntity.ok(response);
  }
  
  /**
   * 商品維護列表
   * 
   * @return
   */
  @GetMapping("/products/list")
  public ResponseEntity<Outbound> productList() {
	    Outbound resp = productService.productList();
	    return ResponseEntity.ok(resp);
	  }

	/**
	* 刪除產品
	* 
	* @param id
	* @return
	*/
	@PutMapping("/deleteProduct/{id}")
	public ResponseEntity<Outbound> deleteProduct(@PathVariable("id") Integer id) {
	    Outbound resp = productService.deleteProduct(id);
	    return ResponseEntity.ok(resp);
	  }

	
}
