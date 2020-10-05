package com.benson.swagger.api.enums.base;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 基础枚举类
 *
 * @param <T>
 * @author zhangby
 * @date 27/9/19 6:03 pm
 */
public interface BaseEnum<T extends BaseEnum> extends IEnum<String> {

    /**
     * 枚举数据库存储值
     */
    String getLabel();

    @Override
    String getValue();

    /**
     * 格式化枚举
     *
     * @return
     */
    @JsonValue
    default String getEnums() {
        return getValue();
    }

    default Dict toDict() {
        return Dict.parse(this);
    }
}
