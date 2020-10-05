package com.benson.swagger.api.entity.postman;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RequestBody {
    private String mode;
    private List<RequestBodyUrlencoded> urlencoded;
    private String raw;
}
