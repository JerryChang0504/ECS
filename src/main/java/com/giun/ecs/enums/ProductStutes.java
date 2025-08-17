package com.giun.ecs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStutes {
    ONSALE("2", "銷售中"),
    STOPED("1", "停售"),
    DELETE("0", "刪除");

    private final String code;
    private final String desc;

    // 取得產品狀態描述
    public static String getDesc(String code) {
        for (ProductStutes productStutes : ProductStutes.values()) {
            if (productStutes.getCode().equals(code)) {
                return productStutes.getDesc();
            }
        }
        return code;
    }

}
