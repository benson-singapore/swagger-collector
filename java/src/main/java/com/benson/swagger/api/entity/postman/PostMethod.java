package com.benson.swagger.api.entity.postman;

import java.util.stream.Stream;

/**
 * Method
 *
 * @author zhangby
 * @date 11/9/20 1:27 pm
 */
public enum PostMethod {
    /**
     * Method
     */
    GET, POST, PUT, DELETE;

    public static PostMethod init(String value) {
        return Stream.of(values())
                .filter(item -> item.toString().equalsIgnoreCase(value))
                .findFirst().orElse(null);
    }
}
