package com.giun.ecs.service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.giun.ecs.dto.request.ProductUploadRequest;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.dto.response.ProductResponse;
import com.giun.ecs.entity.Product;
import com.giun.ecs.repository.ProductRepository;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  public Product saveProduct(ProductUploadRequest req) {
    byte[] imageBytes = null;
    String imageType = req.getImageType();

    // 檢查是否有 Base64 圖片資料
    if (req.getImageBase64() != null && !req.getImageBase64().isBlank()) {
      String base64 = req.getImageBase64();

      // 移除 Data URI scheme 前綴
      if (base64.contains(",")) {
        // 同時嘗試解析圖片類型
        if (imageType == null && base64.startsWith("data:")) {
          int start = base64.indexOf(":") + 1;
          int end = base64.indexOf(";");
          if (start > 0 && end > start) {
            imageType = base64.substring(start, end);
          }
        }
        base64 = base64.substring(base64.indexOf(",") + 1);
      }

      // 錯誤處理：如果 Base64 格式不正確，捕捉例外
      try {
        imageBytes = Base64.getDecoder().decode(base64);
      } catch (IllegalArgumentException e) {
        // 紀錄錯誤或拋出自訂例外，這裡以拋出 RuntimeException 為例
        throw new RuntimeException("無效的 Base64 圖片格式", e);
      }
    }

    Product product = Product.builder()
        .name(req.getName())
        .category(req.getCategory())
        .description(req.getDescription())
        .price(req.getPrice())
        .imageData(imageBytes)
        .imageType(imageType)
        .build();

    return productRepository.save(product);
  }

  public Outbound getProductById(Integer id) {
    Product product =
        productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

    ProductResponse response = ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .category(product.getCategory())
        .rating(null) // TODO: 根據實際資料庫欄位填入 product.getRating()
        .imageBase64(product.getImageData() != null && product.getImageType() != null
            ? "data:" + product.getImageType() + ";base64,"
                + Base64.getEncoder().encodeToString(product.getImageData())
            : null)
        .build();

    return Outbound.ok(response);
  }

  public Outbound getAllProducts() {
    List<ProductResponse> result = productRepository.findAll().stream().map(product -> {
      String imageBase64 = null;
      // 避免不必要的編碼操作
      if (product.getImageData() != null && product.getImageType() != null) {
        String base64 = Base64.getEncoder().encodeToString(product.getImageData());
        imageBase64 = "data:" + product.getImageType() + ";base64," + base64;
      }

      return new ProductResponse(
          product.getId(),
          product.getName(),
          product.getDescription(),
          product.getPrice(),
          product.getCategory(),
          null, // TODO: 根據實際資料庫欄位填入 product.getRating()
          imageBase64);
    }).collect(Collectors.toList());

    return Outbound.ok(result);
  }

  public Outbound updateProduct(Integer id, ProductUploadRequest req) {
    Product product =
        productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

    byte[] imageBytes = null;
    String imageType = req.getImageType();
    // 檢查是否有 Base64 圖片資料
    if (req.getImageBase64() != null && !req.getImageBase64().isBlank()) {
      String base64 = req.getImageBase64();

      // 移除 Data URI scheme 前綴
      if (base64.contains(",")) {
        // 同時嘗試解析圖片類型
        if (imageType == null && base64.startsWith("data:")) {
          int start = base64.indexOf(":") + 1;
          int end = base64.indexOf(";");
          if (start > 0 && end > start) {
            imageType = base64.substring(start, end);
          }
        }
        base64 = base64.substring(base64.indexOf(",") + 1);
      }

      // 錯誤處理：如果 Base64 格式不正確，捕捉例外
      try {
        imageBytes = Base64.getDecoder().decode(base64);
      } catch (IllegalArgumentException e) {
        // 紀錄錯誤或拋出自訂例外，這裡以拋出 RuntimeException 為例
        throw new RuntimeException("無效的 Base64 圖片格式", e);
      }
    }

    Product updateProduct = Product.builder()
        .id(product.getId())
        .name(req.getName())
        .category(req.getCategory())
        .description(req.getDescription())
        .price(req.getPrice())
        .imageData(imageBytes)
        .imageType(imageType)
        .build();


    productRepository.save(updateProduct);

    return Outbound.ok("Product updated successfully");
  }
}


