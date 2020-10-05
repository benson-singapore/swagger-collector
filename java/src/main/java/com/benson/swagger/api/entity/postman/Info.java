package com.benson.swagger.api.entity.postman;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Info {
    private String name;
}
