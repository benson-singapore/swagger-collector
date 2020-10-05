package com.benson.swagger.api;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.*;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.In;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.StringProperty;
import org.assertj.core.util.Lists;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CreateSwagger {
    public static void main(String[] args) throws JsonProcessingException {
        Swagger swagger = new Swagger()
                .host("161.117.176.103:8010")
                .info(new Info()
                        .description("权限模块接口")
                        .version("1.0")
                        .title("Security API DOC")
                )
                .basePath("/")
                .tags(Lists.newArrayList(
                        new Tag()
                                .name("授权管理")
                                .description("Sys Auth Controller")
                ))
                .securityDefinition("Authorization", new ApiKeyAuthDefinition("Authorization", In.HEADER));
        // 设置参数
        Map<String, Path> pathMap = new LinkedHashMap<>();
        pathMap.put("/sys/oauth/login", new Path()
                .get(new Operation()
                        .tag("授权管理")
                        .summary("登录授权")
                        .description("登录授权接口，获取token")
                        .produces(Lists.newArrayList(
                                "*/*",
                                "application/json"
                        ))
                        .parameter(new QueryParameter()
                                .name("password")
                                .description("密码")
                                .type("string")
                                .example("123456")
                        )
                        .parameter(new QueryParameter()
                                .name("username")
                                .description("用户名")
                                .type("string")
                                .example("admin")
                        )
                        .response(200, new Response().description("ok"))
                        .security(new SecurityRequirement("Authorization")
                                .scope("global")
                        )
                        .deprecated(false)
                )
        );
        swagger.paths(pathMap);
        //String id,
        //      String name,
        //      ResolvedType type,
        //      String qualifiedType,
        //      Map<String, ModelProperty> properties,
        //      String description,
        //      String baseModel,
        //      String discriminator,
        //      List<ModelReference> subTypes,
        //      Object example,
        //      Xml xml
        swagger.addDefinition("Params", new ModelImpl()
                .type("object")
                .description("Params 对象")
                .property("flag", new StringProperty())

        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = mapper.writeValueAsString(swagger);
        System.out.println(json);
    }
}
