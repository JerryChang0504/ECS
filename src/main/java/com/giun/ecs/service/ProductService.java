package com.giun.ecs.service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.giun.ecs.dto.request.ProductUploadRequest;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.dto.response.ProductListResp;
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
    Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

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
    List<ProductListResp> result = productRepository.findAll().stream().map(product -> {

      return ProductListResp.builder()
          .id(product.getId())
          .name(product.getName())
          .description(product.getDescription())
          .price(product.getPrice())
          .category(product.getCategory())
          .rating(null) // TODO: 根據實際資料庫欄位填入 product.getRating()
          .imageBase64(generateImageBase64(product.getImageData(), product.getImageType()))
          .build();
    }).collect(Collectors.toList());

    return Outbound.ok(result);
  }

  public Outbound updateProduct(Integer id, ProductUploadRequest req) {
    Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

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

  public Outbound getProductsManage() {
    List<ProductResponse> products = productRepository.findAll().stream().map(product -> {

      return ProductResponse.builder()
          .id(product.getId())
          .name(product.getName())
          .description(product.getDescription())
          .price(product.getPrice())
          .stock(product.getStock())
          .category(product.getCategory())
          .imageBase64(generateImageBase64(product.getImageData(), product.getImageType()))
          .states(product.getStates())
          .build();
    }).collect(Collectors.toList());
    return Outbound.ok(products);
  }

  /**
   * 用來傳遞圖片處理結果的 record。 Record 是 Java 14+ 的特性，適合用來傳遞不可變的資料物件。
   */
  private record ImageInfo(byte[] imageData, String imageType) {
  }

  /**
   * 處理 Base64 圖片字串，解析出圖片二進制資料和類型。
   * 
   * @param base64String      Base64 編碼的圖片字串，可包含 Data URI 前綴。
   * @param existingImageType 已知或預設的圖片類型。
   * @return 包含圖片資料和類型的 ImageInfo 物件。
   */
  private ImageInfo processBase64Image(String base64String, String existingImageType) {
    if (base64String == null || base64String.isBlank()) {
      return new ImageInfo(null, null); // 沒有圖片，返回空值
    }

    String imageType = existingImageType;
    String base64Content = base64String;

    // 移除 Data URI scheme 前綴並嘗試解析圖片類型
    if (base64String.startsWith("data:")) {
      int commaIndex = base64String.indexOf(',');
      if (commaIndex != -1) {
        String dataUri = base64String.substring(0, commaIndex);
        if (dataUri.contains(";base64")) {
          imageType = dataUri.substring(dataUri.indexOf(':') + 1,
              dataUri.indexOf(';'));
        }
        base64Content = base64String.substring(commaIndex + 1);
      }
    }

    try {
      byte[] imageBytes = Base64.getDecoder().decode(base64Content);
      return new ImageInfo(imageBytes, imageType);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("無效的 Base64 圖片格式", e);
    }
  }

  /**
   * 產生圖片 Base64 字串
   * 
   * @param imageData 圖片資料
   * @param imageType 圖片類型
   * @return
   */
  private String generateImageBase64(byte[] imageData, String imageType) {
    return imageData != null && imageType != null
        ? "data:" + imageType + ";base64,"
            + Base64.getEncoder().encodeToString(imageData)
        : null;
  }
}