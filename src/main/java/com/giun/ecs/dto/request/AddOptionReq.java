package com.giun.ecs.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddOptionReq {

    private String ListName;
    private String Name;
    private String Value;
    private Integer SortOrder;
    private Boolean IsActive;
    private String Description;

}