package com.benson.swagger.api.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.setting.Setting;

/**
 * 获取公用配置文件
 *
 * 
 * @author zhangby
 * @date 2019-05-13 16:13
 */
public class ConfigUtil {
    /**
     * 异常错误码配置
     */
    private static Setting errorSetting = null;

    /**
     * 获取错误码配置
     */
    public static Setting getErrorSetting() {
        return configFunction("error.setting", errorSetting);
    }

    /**
     * 读取系统配置信息
     *
     * @param configName
     * @return
     */
    private static Setting configFunction(String configName, Setting prop) {
        if (ObjectUtil.isNull(prop)) {
            prop = new Setting(configName);
        }
        return prop;
    }

}