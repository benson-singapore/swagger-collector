package com.benson.swagger.api.aop;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.benson.swagger.api.constants.Constants;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * API 接口权限安全过滤
 *
 * @author zhangby
 * @date 12/6/20 5:32 pm
 */
@Component
@WebFilter(urlPatterns = "/api/**", filterName = "loginFilter")
public class ApiSecurityFilter implements Filter {

    /**
     * 排除过滤请求
     */
    public static final List<String> filterList = CollectionUtil.newArrayList(
            "/api/creator/**",
            "/api/datasource/**",
            "/api/swagger/**"
    );

    /**
     * 过滤器
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url = request.getRequestURI();
        if (new AntPathMatcher().match("/api/**", url) && filterUrl(url)) {
            // 获取当前登录用户
            Object loginName = request.getSession().getAttribute(Constants.CURRENT_USER);
            // 验证IP白名单
            if (ObjectUtil.isNull(loginName)) {
                returnJson(servletResponse, "998", "请重新登录！");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 过滤url
     */
    public Boolean filterUrl(String url) {
        for (String item : filterList) {
            if (new AntPathMatcher().match(item, url)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回信息
     */
    private void returnJson(ServletResponse response, String code, String message) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            Dict data = Dict.create().set("code", code).set("msg", message);
            out = response.getWriter();
            out.append(JSONUtil.parseObj(data).toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
