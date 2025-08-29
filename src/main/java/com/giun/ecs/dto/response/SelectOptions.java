package com.giun.ecs.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SelectOptions {

  private String label;
  private String value;
  private Integer sortOrder;
}
