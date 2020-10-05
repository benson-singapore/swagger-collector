package com.benson.swagger.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger config
 *
 * @author zhangby
 * @date 2019-05-14 15:08
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    /**
     * Determine if swagger is on
     */
    @Value(value = "${swagger.enabled:false}")
    Boolean swaggerEnabled;

    @Bean
    public Docket api_collect() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo("API 基础管理平台（ Collect API Management ）", "", "1.0"))
                .enable(swaggerEnabled).select()
                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.benson.swagger.api.controller"))
                .build()
                .groupName("vn_ranking_api")
                .pathMapping("/");
    }

    private ApiInfo apiInfo(String title, String desc, String version) {
        return new ApiInfoBuilder()
                .title(title)
                .description(desc)
                .version(version)
                .build();
    }
}
