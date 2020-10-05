package com.benson.swagger.api.entity.postman;

import lombok.Data;
import lombok.experimental.Accessors;

/** query */
@Data
@Accessors(chain = true)
public class RequestQuery {
    private String key;
    private String value;
    private String description;
}
