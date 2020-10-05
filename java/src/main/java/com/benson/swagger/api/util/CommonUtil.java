package com.benson.swagger.api.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.setting.Setting;
import com.benson.swagger.api.entity.base.ResultPoJo;
import com.benson.swagger.api.exception.MyBaselogicException;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 常用工具类
 *
 * @author zhangby
 * @date 2019-05-13 12:20
 */
public class CommonUtil {

    /**
     * list数据转换
     *
     * @param list list对象
     * @param func lamdba 表达式 function
     * @param <E>  原对象
     * @param <T>  转换完的对象
     * @return List<E>
     */
    public static <E, T> List<E> convers(List<T> list, Function<T, E> func) {
        return list.stream().collect(ArrayList::new, (li, p) -> li.add(func.apply(p)), List::addAll);
    }

    /**
     * 加载异常信息
     *
     * @return ResultPoJo
     * @author zhangby
     * @date 2018/11/6 11:08 AM
     */
    public static ResultPoJo loadException2ResultPoJo(MyBaselogicException exception) {
        return loadException2ResultPoJo(exception.getNum(), exception.getMsg());
    }

    /**
     * 加载异常信息
     *
     * @return ResultPoJo
     * @author zhangby
     * @date 2018/11/6 11:08 AM
     */
    public static ResultPoJo loadException2ResultPoJo(String num, Object... msg) {
        return ResultPoJo.ok().setCode(num).setMsg(loadErrorMsg(num, msg));
    }

    /**
     * 加载错误信息
     *
     * @return
     */
    public static String loadErrorMsg(String num, Object... msg) {
        //获取错误码配置信息
        Setting errorSetting = ConfigUtil.getErrorSetting();
        //获取错误码
        String errMsg = errorSetting.get(StrUtil.format("code_{}", num));
        if (ObjectUtil.isNotNull(msg)) {
            errMsg = StrUtil.format(errMsg, msg);
        }
        return errMsg;
    }

    /**
     * 字符串模板替换 截取
     * ClassTest::getDictList4Function,{}::{} ->[ClassTest,getDictList4Function]
     *
     * @param str
     * @param temp
     * @return
     */
    public static List<String> splitStr4Temp(String str, String temp) {
        List<String> rsList = Lists.newArrayList();
        Iterator<String> iterator = Splitter.on("{}").omitEmptyStrings().split(temp).iterator();
        while (iterator.hasNext()) {
            str = str.replace(iterator.next(), "〆");
        }
        Iterator<String> split = Splitter.on("〆").omitEmptyStrings().split(str).iterator();
        while (split.hasNext()) {
            rsList.add(split.next());
        }
        return rsList.stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
    }

    /**
     * 异常捕获
     *
     * @param resolver resolver
     * @param <T>      T
     * @return
     */
    public static <T> Optional<T> resolve(Supplier<T> resolver) {
        Optional<T> optional = Optional.empty();
        try {
            T result = resolver.get();
            optional = Optional.ofNullable(result);
        } catch (Exception e) {
            optional = Optional.empty();
        }
        return optional;
    }

    public static Optional<String> notEmpty(String string) {
        return Optional.ofNullable(string).filter(StrUtil::isNotBlank);
    }

    public static Optional<String> isEmpty(String string) {
        return Optional.ofNullable(string).filter(StrUtil::isBlank);
    }

    public static String getIpAddress(HttpServletRequest request) {
        String[] headers = { "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };
        String ip = ServletUtil.getClientIP(request,headers);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}