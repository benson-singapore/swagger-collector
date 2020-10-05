package com.benson.swagger.api.entity.postman;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import io.swagger.models.*;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.QueryParameter;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class PostmanMethod {
    private String name;
    private RequestItem request;
    private Map<String, Object> response;


    public String getUrl() {
        return "/" + String.join("/", request.getUrl().getPath());
    }

    /**
     * 获取swagger Path对象
     */
    public Path getSwaggerPath(String tag) {
        Path path = new Path();
        Operation operation = getSwaggerOperation(tag);
        switch (request.getMethod()) {
            case GET:
                path.get(operation);
                break;
            case POST:
                path.post(operation);
                break;
            case PUT:
                path.put(operation);
                break;
            case DELETE:
                path.delete(operation);
                break;
        }
        return path;
    }

    /**
     * 获取swagger Operation对象
     */
    public Operation getSwaggerOperation(String tag) {
        Operation operation = new Operation()
                .tag(tag)
                .summary(name)
                .description(name)
                .produces(Lists.newArrayList(
                        "*/*",
                        "application/json"
                ))
                .response(200, new Response().description("ok"))
                .security(new SecurityRequirement("Authorization")
                        .scope("global")
                )
                .deprecated(false);
        // 获取参数
        RequestBody body = request.getBody();
        RequestUrl requestUrl = request.getUrl();
        if (ObjectUtil.isNull(body) && ObjectUtil.isNotNull(requestUrl)) {
            List<RequestQuery> query = requestUrl.getQuery();
            if (ObjectUtil.isNotNull(query) && !query.isEmpty()) {
                // 设置参数
                query.forEach(item -> {
                    operation.parameter(new QueryParameter()
                            .name(item.getKey())
                            .description(item.getDescription())
                            .example(item.getValue())
                    );
                });
            }
        } else {
            if (body.getMode().equalsIgnoreCase("urlencoded")) {
                List<RequestBodyUrlencoded> urlencodedList = body.getUrlencoded();
                if (ObjectUtil.isNotNull(urlencodedList) && !urlencodedList.isEmpty()) {
                    urlencodedList.forEach(item -> {
                        operation.parameter(new QueryParameter()
                                .name(item.getKey())
                                .type(item.getType().equals("text") ? "string" : item.getType())
                                .description(item.getDescription())
                                .example(item.getValue())
                        );
                    });
                }
            }
            if (body.getMode().equalsIgnoreCase("raw")) {
                operation
                        .consumes(Lists.newArrayList(
                                "application/json"
                        ))
                        .parameter(new BodyParameter()
                        .name("body")
                        .description(body.getRaw())
                        .schema(new ModelImpl()
                                .type("object")
                                .description("Params 对象")
                                .example(body.getRaw())
                        )
                );
            }
        }
        return operation;
    }
}
