package com.benson.swagger.api.enums;

import com.benson.swagger.api.enums.base.BaseEnum;

/**
 * 请求方法枚举
 *
 * @author zhangby
 * @date 14/9/20 12:07 pm
 */
public enum MethodEnum implements BaseEnum {
    /**
     * 请求方法枚举
     */
    GET("GET", "1"),
    POST("POST", "2"),
    PUT("PUT", "3"),
    PATCH("PATCH", "4"),
    DELETE("DELETE", "5"),
    ;

    private String label;
    private String value;

    MethodEnum(String label, String value) {
        this.label = label;
        this.value = value;
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
