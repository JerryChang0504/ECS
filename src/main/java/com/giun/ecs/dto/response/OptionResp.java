package com.giun.ecs.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OptionResp {

    private Integer id;
    private String listName;
    private String key;
    private String value;
    private Integer sortOrder;
    private Boolean isActive;
    private String description;

}
