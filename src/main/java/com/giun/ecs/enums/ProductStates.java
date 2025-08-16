package com.giun.ecs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStates {
    ONSALE("2", "銷售中"),
    STOPED("1", "停售"),
    DELETE("0", "刪除");

    private final String code;
    private final String desc;

    // 取得產品狀態描述
    public static String getDesc(String code) {
        for (ProductStates productStates : ProductStates.values()) {
            if (productStates.getCode().equals(code)) {
                return productStates.getDesc();
            }
        }
        return code;
    }

}
