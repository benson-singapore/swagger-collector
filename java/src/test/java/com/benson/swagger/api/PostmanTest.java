package com.benson.swagger.api;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.benson.swagger.api.entity.postman.Postman;
import com.benson.swagger.api.entity.postman.PostmanItem;
import com.benson.swagger.api.entity.postman.PostmanMethod;
import com.benson.swagger.api.entity.postman.RequestItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import sun.rmi.runtime.Log;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public class PostmanTest {
    public static void main(String[] args) throws JsonProcessingException {
        JSONObject json = JSONUtil.readJSONObject(new File("/Users/zhangbiyu/Downloads/kaiyuan.json"), Charset.forName("utf-8"));
        // 转换对象
        Postman postman = new Postman();
        // 对象循环转换
        List<JSONObject> itemJsonObject = json.getJSONArray("item").toList(JSONObject.class);
        itemJsonObject.forEach(item -> {
            PostmanItem postmanItem = new PostmanItem().addName(item.getStr("name"));
            parseResult(item, postmanItem);
            postman.addItem(postmanItem);
        });
        System.out.println(JSONUtil.parseObj(postman).toStringPretty());
    }

    /**
     * 迭代转换
     */
    public static PostmanItem parseResult(JSONObject json, PostmanItem postmanItem) {
        if (json.containsKey("item")) {
            List<JSONObject> itemList = json.getJSONArray("item").toList(JSONObject.class);
            if (!itemList.isEmpty()) {
                for (JSONObject item : itemList) {
                    if (item.containsKey("request")) {
                        // 格式化 request
                        postmanItem.addItem(item.toBean(PostmanMethod.class));
                    } else {
                        postmanItem.addName(item.getStr("name"));
                        return parseResult(item, postmanItem);
                    }
                }
            }
        }

        return postmanItem;
    }
}
