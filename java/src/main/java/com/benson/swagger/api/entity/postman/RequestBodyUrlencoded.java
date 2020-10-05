package com.benson.swagger.api.entity.postman;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RequestBodyUrlencoded {
    private String key;
    private String value;
    private String type;
    private String description;
}
