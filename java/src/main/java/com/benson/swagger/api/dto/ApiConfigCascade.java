package com.benson.swagger.api.dto;

import cn.hutool.core.bean.BeanUtil;
import com.benson.swagger.api.entity.ApiConfig;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * API 配置级联对象
 *
 * @author zhangby
 * @date 8/9/20 5:57 pm
 */
@Data
@Accessors(chain = true)
public class ApiConfigCascade {
    private String id;
    private String parentId;
    private String autoAuthId;
    private String name;
    private String value;
    private String host;
    private List<ApiConfigCascade> children;

    /**
     * 格式化对象
     */
    public static ApiConfigCascade parseEntity(ApiConfig apiConfig) {
        ApiConfigCascade apiConfigCascade = new ApiConfigCascade();
        BeanUtil.copyProperties(apiConfig, apiConfigCascade);
        return apiConfigCascade;
    }
}
