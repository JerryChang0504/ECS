package com.giun.ecs.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "新增選項資料")
public class OptionReq {

  @Schema(description = "分類選項名稱", example = "category")
  private String listName;
  @Schema(description = "選項名稱", example = "電子產品")
  private String name;
  @Schema(description = "選項值", example = "1")
  private String value;
  @Schema(description = "排序", example = "0")
  private Integer sortOrder;
  @Schema(description = "是否啟用", example = "true")
  private Boolean isActive;
  @Schema(description = "描述", example = "商品分類")
  private String description;

}
