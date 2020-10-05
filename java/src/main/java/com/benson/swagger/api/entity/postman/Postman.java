package com.benson.swagger.api.entity.postman;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.models.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class Postman {
    private Info info;
    private List<PostmanItem> item;

    public Postman addItem(PostmanItem itemResult) {
        item = Optional.ofNullable(item).orElse(new ArrayList<>());
        item.add(itemResult);
        return this;
    }

    /**
     * 格式化json
     */
    public static Postman parse4Json(String jsonStr) {
        try {
            JSONObject json = JSONUtil.parseObj(jsonStr);
            // 转换对象
            Postman postman = new Postman();
            postman.setInfo(json.getJSONObject("info").toBean(Info.class));
            // 对象循环转换
            List<JSONObject> itemJsonObject = json.getJSONArray("item").toList(JSONObject.class);
            itemJsonObject.forEach(item -> {
                PostmanItem postmanItem = new PostmanItem().addName(item.getStr("name"));
                parseResult(item, postmanItem);
                postman.addItem(postmanItem);
            });
            return postman;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 迭代转换
     */
    private static PostmanItem parseResult(JSONObject json, PostmanItem postmanItem) {
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

    /**
     * 返回swagger tags
     */
    public List<Tag> getTags() {
        return getItem().stream()
                .map(item -> new Tag().name(String.join("-", item.getName())))
                .collect(Collectors.toList());
    }
}
