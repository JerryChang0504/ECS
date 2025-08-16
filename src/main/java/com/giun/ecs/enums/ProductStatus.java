package com.giun.ecs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {
    ONSALE("2", "銷售中"),
    STOPED("1", "停售"),
    DELETED("0", "已刪除");

    private final String code;
    private final String des;

    // 取得產品描述
    public static String getDescription(String code) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.getCode().equals(code)) {
                return status.getDes();
            }
        }
        return code;
    }
}
