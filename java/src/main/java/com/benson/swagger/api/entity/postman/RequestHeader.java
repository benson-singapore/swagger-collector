package com.benson.swagger.api.entity.postman;

import lombok.Data;
import lombok.experimental.Accessors;

/** header */
@Data
@Accessors(chain = true)
public class RequestHeader {
    private String key;
    private String value;
}
