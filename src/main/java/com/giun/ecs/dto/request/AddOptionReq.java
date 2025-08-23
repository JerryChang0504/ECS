package com.giun.ecs.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "新增選項請求")
public class AddOptionReq {
    @Schema(description = "選項分類", example = "category")
    private String listName;

    @Schema(description = "選項名稱", example = "category")
    private String optionName;

    @Schema(description = "選項值", example = "手機")
    private String optionValue;

    @Schema(description = "選項排序", example = "1")
    private Integer sortOrder;

    @Schema(description = "是否啟用", example = "true")
    private Boolean isActive;

    @Schema(description = "選項列表描述", example = "手機類別")
    private String description;
}
