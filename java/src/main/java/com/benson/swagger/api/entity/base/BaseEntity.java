package com.benson.swagger.api.entity.base;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.benson.swagger.api.constants.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 基础类
 *
 * @author zhangby
 * @date 23/11/19 6:28 pm
 */
public abstract class BaseEntity<T extends Model<T>> extends Model<T> {
    /**
     * 多余属性
     */
    @TableField(select = false,exist = false)
    private Dict extMap = Dict.create();

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    public Object get(String key) {
        return extMap.get(key);
    }

    /**
     * 设置值
     * @param key
     * @param val
     */
    public void set(String key, Object val) {
        extMap.set(key, val);
    }


    /**
     * 插入预处理
     */
    public void preInsert() {
        //setId for uuid
        excuteMethod(this, "setId",String.class, IdUtil.simpleUUID());
        //set current user
        String currentId = Constants.CURRENT_USER_ID;
        excuteMethod(this, "setCreateBy",String.class, currentId);
        excuteMethod(this, "setCreateDate", Date.class,new Date());
        excuteMethod(this, "setUpdateBy",String.class, currentId);
        excuteMethod(this, "setUpdateDate",Date.class,new Date());
        excuteMethod(this, "setDelFlag",String.class,"0");
    }

    /**
     * 更新预处理
     */
    public void preUpdate() {
        //set current user
        String currentId = Optional.ofNullable(System.getProperty(Constants.CURRENT_USER_ID))
                .filter(StrUtil::isNotBlank).orElse("1");
        excuteMethod(this, "setUpdateBy",String.class, currentId);
        excuteMethod(this, "setUpdateDate",Date.class,new Date());
    }

    private void excuteMethod(Object obj, String methodName, Class paramTypes,Object... param) {
        try {
            Class<?> aClass = obj.getClass();
            Method method = ReflectUtil.getMethod(aClass, methodName,paramTypes);
            if (ObjectUtil.isNotNull(method)) {
                method.invoke(obj, param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiModelProperty(hidden = true)
    public Map<String, Object> getTails() {
        return extMap;
    }
    @ApiModelProperty(hidden = true)
    public void setTails(Map<String, Object> map) {
        extMap.putAll(map);
    }

    @Override
    public String toString() {
        return JSONUtil.parseObj(this).toString();
    }

    @JsonValue
    public Object getJson() {
        Field[] fields = ReflectUtil.getFields(this.getClass());
        /** 处理枚举 */
        Stream.of(fields)
                .filter(field -> ObjectUtil.isNotNull(field.getAnnotation(EnumFormat.class)))
                .forEach(field -> {
                    try {
                        String name = field.getName();
                        String getMethod = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                        Method methodByName = ReflectUtil.getMethodByName(this.getClass(), getMethod);
                        Object invoke = methodByName.invoke(this);
                        if (ObjectUtil.isNotNull(invoke)) {
                            Class<?> enumType = field.getType();
                            Enum<?>[] enums = (Enum[]) enumType.getEnumConstants();
                            Method valueMethod = ReflectUtil.getMethod(enumType, "getValue");
                            Object value = valueMethod.invoke(invoke);
                            Method labelMethod = ReflectUtil.getMethod(enumType, "getLabel");
                            Object label = labelMethod.invoke(invoke);
                            Dict dict = Dict.create().set("label", label).set("value", value);
                            this.set(name + "Enum", dict);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        // 过滤
        Dict parse = Dict.parse(this);
        List<String> keys = Stream.of(fields)
                .filter(field -> ObjectUtil.isNotNull(field.getAnnotation(JsonIgnore.class)))
                .map(Field::getName)
                .collect(Collectors.toList());
        keys.forEach(key -> parse.remove(key));
        parse.put("tails", extMap);
        return parse;
    }
}
