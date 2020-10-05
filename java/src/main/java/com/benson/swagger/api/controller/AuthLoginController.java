package com.benson.swagger.api.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.benson.common.creator.entity.ResultPoJo;
import com.benson.swagger.api.constants.Constants;
import com.benson.swagger.api.entity.config.ApiInfoConfig;
import com.benson.swagger.api.exception.MyBaselogicException;
import com.benson.swagger.api.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * 授权登录管理
 *
 * @author zhangby
 * @date 30/8/20 4:12 pm
 */
@RestController
@RequestMapping("/")
@Api(tags = "授权登录管理 （ Authorized login management ）")
public class AuthLoginController {

    @Autowired
    private ApiInfoConfig apiInfoConfig;

     /**
      * 获取当前登录用户
      * @return
      */
     @GetMapping("/api/currentUser")
     @ApiOperation(value = "获取当前登录用户", notes = "获取当前登录用户", produces = "application/json")
     public ResultPoJo<Dict> getCurrentUser(@ApiIgnore HttpServletRequest request) {
         Object loginName = request.getSession().getAttribute(Constants.CURRENT_USER);
         return ResultPoJo.ok(Dict.create()
                 .set("name", loginName)
                 .set("avatar", "https://em.aseanlotto.com/image/2020/09/1bf9e0fe04994c959878b99642a2e4f3.png")
                 .set("id", "1")
                 .set("access", "admin")
         );
     }

    /**
     * 登录验证
     *
     * @return
     */
    @GetMapping("/login/verify")
    @ApiOperation(value = "登录验证", notes = "登录验证：flag【true 成功】，【false 失败】", produces = "application/json")
    public ResultPoJo loginVerify(@ApiIgnore HttpServletRequest request) {
        Object loginName = request.getSession().getAttribute(Constants.CURRENT_USER);
        if (ObjectUtil.isNotNull(loginName)) {
            return ResultPoJo.ok(true);
        }
        return ResultPoJo.ok(false);
    }

    /**
     * 用户登录
     *
     * @return
     */
    @GetMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户登录", produces = "application/json")
    public ResultPoJo login(
            @RequestParam("loginName") String loginName,
            @RequestParam("password") String password,
            @ApiIgnore HttpServletRequest request
    ) {
        String name = CommonUtil.notEmpty(loginName).orElseThrow(() -> new MyBaselogicException("111"));
        String pwd = CommonUtil.notEmpty(password).orElseThrow(() -> new MyBaselogicException("112"));
        if (ObjectUtil.isNull(apiInfoConfig)) {
            // 未配置登录用户信息
            throw new MyBaselogicException("113");
        }
        if (!name.equals(apiInfoConfig.getLoginName())) {
            // 用户不存在
            throw new MyBaselogicException("100");
        }
        if (!pwd.equals(apiInfoConfig.getPassword())) {
            // 密码错误
            throw new MyBaselogicException("103");
        }
        // 返回用户信息，设置 session
        request.getSession().setAttribute(Constants.CURRENT_USER, name);
        return ResultPoJo.ok(Dict.create().set("loginName", name));
    }


     /**
      * 退出登录
      * @return
      */
     @GetMapping("/logout")
     @ApiOperation(value = "退出登录", notes = "退出登录", produces = "application/json")
     public ResultPoJo logout(@ApiIgnore HttpServletRequest request) {
         request.getSession().removeAttribute(Constants.CURRENT_USER);
         return ResultPoJo.ok();
     }
}
