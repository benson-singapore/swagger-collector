package com.benson.swagger.api;

import com.benson.common.creator.annotation.EnableCreatorServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 项目启动项
 *
 * @author zhangby
 * @date 27/8/20 5:16 pm
 */
@EnableCreatorServer
@SpringBootApplication(scanBasePackages = "com.benson.*")
@MapperScan("com.benson.swagger.api.mapper")
@Controller
public class CollectApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollectApiApplication.class, args);
    }

    @RequestMapping("/swagger/**")
    public String BB(HttpServletRequest request) {
        return "forward:/" + request.getRequestURI().replace("/swagger", "");
    }

    @RequestMapping("")
    public String index(HttpServletRequest request) {
        return "🙅不用尝试";
    }
}
