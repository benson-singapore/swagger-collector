package com.benson.swagger.api.entity.postman;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RequestUrl {
    private String raw;
    private List<String> host;
    private List<String> path;
    private List<RequestQuery> query;
}
