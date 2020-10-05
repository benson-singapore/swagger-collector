package com.benson.swagger.api.enums;

import com.benson.swagger.api.enums.base.BaseEnum;

/**
 * 状态枚举
 *
 * @author zhangby
 * @date 2/9/20 5:14 pm
 */
public enum StatusEnum implements BaseEnum {
    //状态：0 正常 1 停用
    Normal("正常", "0"),
    Deactivate("停用", "1"),
    ;

    private String label;
    private String value;

    StatusEnum(String label, String value) {
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
