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
@Schema(description = "選單")
public class CategoriesResponse {
    @Schema(description = "選單ID")
    private Integer id;
    @Schema(description = "選單分類")
    private String listName;
    @Schema(description = "選單名稱")
    private String lable;
    @Schema(description = "選單值")
    private String value;
    @Schema(description = "選單排序")
    private Integer sortOrder;
    @Schema(description = "是否啟用")
    private Boolean isActive;
    @Schema(description = "選單描述")
    private String description;
}
