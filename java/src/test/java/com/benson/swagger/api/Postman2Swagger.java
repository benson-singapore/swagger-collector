package com.benson.swagger.api;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.benson.swagger.api.entity.postman.Postman;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.*;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.In;
import io.swagger.models.properties.StringProperty;
import org.assertj.core.util.Lists;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Postman 转换 Swagger
 *
 * @author zhangby
 * @date 11/9/20 2:40 pm
 */
public class Postman2Swagger {
    public static void main(String[] args) throws JsonProcessingException {
        JSONObject json = JSONUtil.readJSONObject(new File("/Users/zhangbiyu/Downloads/asd.json"), Charset.forName("utf-8"));
        Postman postman = Postman.parse4Json(json.toString());
        System.out.println(JSONUtil.parseObj(postman).toString());

        Swagger swagger = new Swagger()
                .host("161.117.176.103:8010")
                .info(new Info()
                        .description(postman.getInfo().getName())
                        .version("1.0")
                        .title(postman.getInfo().getName())
                )
                .basePath("/")
                .securityDefinition("Authorization", new ApiKeyAuthDefinition("Authorization", In.HEADER));
        // 添加entity
        swagger.addDefinition("Params", new ModelImpl()
                .type("object")
                .description("Params 对象")
                .property("flag", new StringProperty())

        );
        // 设置参数
        List<Tag> tags = postman.getTags();
        // 设置参数
        Map<String, Path> pathMap = new LinkedHashMap<>();
        postman.getItem().forEach(item -> {
            String tag = String.join("-", item.getName());
            item.getItem().forEach(itemMethod -> {
                Path path = itemMethod.getSwaggerPath(tag);
                pathMap.put(itemMethod.getUrl(), path);
            });
        });

        swagger.paths(pathMap);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonSwagger = mapper.writeValueAsString(swagger);
        System.out.println(jsonSwagger);
    }
}
