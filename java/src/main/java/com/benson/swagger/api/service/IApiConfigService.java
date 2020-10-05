package com.benson.swagger.api.service;

import com.benson.swagger.api.dto.ApiConfigCascade;
import com.benson.swagger.api.entity.ApiConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangby
 * @since 2020-08-27
 */
public interface IApiConfigService extends IService<ApiConfig> {

    void saveApiConfig(ApiConfig apiConfig);

    void updateApiConfig(String id, ApiConfig apiConfig);

    Integer getMaxSort(String parentId);

    List<ApiConfigCascade> getApiConfigCascade();
}
