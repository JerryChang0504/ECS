package com.giun.ecs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "產品資訊")
public class ProductShow {
  @Schema(description = "產品ID")
  private Integer id;

  @Schema(description = "產品名稱")
  private String name;

  @Schema(description = "產品價格")
  private Integer price;

  @Schema(description = "產品描述")
  private String description;

  @Schema(description = "產品類別")
  private String category;

  @Schema(description = "產品評價")
  private Double rating;

  @Schema(description = "產品圖片 Base64 字串")
  private String imageBase64; // e.g., "data:image/jpeg;base64,..."
}
