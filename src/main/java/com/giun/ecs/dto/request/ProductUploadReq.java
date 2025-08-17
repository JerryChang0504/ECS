package com.giun.ecs.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "產品上傳請求")
public class ProductUploadReq {


  @Schema(description = "產品名稱", example = "iPhone 13 Pro")
  private String name;

  @Schema(description = "產品類別", example = "手機")
  private String category;

  @Schema(description = "產品價格", example = "9999")
  private Integer price;

  @Schema(description = "產品庫存", example = "10")
  private Integer stock;

  @Schema(description = "產品描述", example = "iPhone 13 Pro")
  private String description;

  @Schema(description = "產品圖片 Base64 字串", example = "iVBORw0KGgoAAAANSUhEUgAA...")
  private String imageBase64;

  @Schema(description = "產品圖片格式", example = "image/png")
  private String imageType;
}
