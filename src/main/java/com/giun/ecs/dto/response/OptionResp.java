package com.giun.ecs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "選項列表")
public class OptionResp {

  @Schema(description = "選項值")
  private String value;
  @Schema(description = "選項名稱")
  private String label;
  @Schema(description = "選項排序")
  private Integer sortOrder;

}
