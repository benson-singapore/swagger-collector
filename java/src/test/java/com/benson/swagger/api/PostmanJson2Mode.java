package com.benson.swagger.api;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * postman 对象转换
 *
 * @author zhangby
 * @date 11/9/20 6:13 pm
 */
public class PostmanJson2Mode {
    public static void main(String[] args) {
        String json = "{\n" +
                "  \"username\": \"yuwu188\"\n" +
                "}";
        JSONObject jsonObject = JSONUtil.parseObj(json);
        Dict dict = jsonObject.toBean(Dict.class);
        System.out.println(dict);
    }
}
