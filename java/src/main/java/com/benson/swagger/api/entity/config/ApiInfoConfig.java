package com.benson.swagger.api.entity.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 登录信息配置
 *
 * @author zhangby
 * @date 30/8/20 4:14 pm
 */
@Component
@ConfigurationProperties(prefix = "basic-common.api")
@Data
public class ApiInfoConfig {
    private String loginName;
    private String password;
}
