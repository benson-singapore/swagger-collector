package com.benson.swagger.api.entity.postman;

import cn.hutool.json.JSONObject;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RequestItem {
    private PostMethod method;
    private List<RequestHeader> header;
    private RequestUrl url;
    private RequestBody body;
}
