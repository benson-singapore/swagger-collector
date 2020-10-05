package com.benson.swagger.api.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.benson.swagger.api.constants.Constants;
import com.benson.swagger.api.dto.ApiConfigCascade;
import com.benson.swagger.api.entity.ApiConfig;
import com.benson.swagger.api.enums.StatusEnum;
import com.benson.swagger.api.mapper.ApiConfigDao;
import com.benson.swagger.api.service.IApiConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.benson.swagger.api.util.CommonUtil;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author zhangby
 * @since 2020-08-27
 */
@Service
public class ApiConfigServiceImpl extends ServiceImpl<ApiConfigDao, ApiConfig> implements IApiConfigService {

    /**
     * 保存
     */
    @Override
    public void saveApiConfig(ApiConfig apiConfig) {
        String parentId = CommonUtil.notEmpty(apiConfig.getParentId())
                .orElse(Constants.DEFAULT_PARENT_ID);
        apiConfig.setParentId(parentId).preInsert();
        apiConfig.insert();
    }

    /**
     * 更新
     */
    @Override
    public void updateApiConfig(String id, ApiConfig apiConfig) {
        apiConfig.setId(id).preUpdate();
        apiConfig.updateById();
    }

    /**
     * 获取最大排序值
     */
    @Override
    public Integer getMaxSort(String parentId) {
        ApiConfig serviceOne = getOne(new LambdaQueryWrapper<ApiConfig>()
                .select(ApiConfig::getSort)
                .eq(ApiConfig::getParentId, CommonUtil.notEmpty(parentId).orElse(Constants.DEFAULT_PARENT_ID))
                .orderByDesc(ApiConfig::getSort)
                .last("limit 1")
        );
        Integer maxSort = Optional.ofNullable(serviceOne)
                .map(ApiConfig::getSort)
                .orElse(0);
        return maxSort;
    }

    /**
     * 级联查询接口配置
     */
    @Override
    public List<ApiConfigCascade> getApiConfigCascade() {
        // 查询数据库
        List<ApiConfigCascade> apiConfigs = list(new LambdaQueryWrapper<ApiConfig>()
                .eq(ApiConfig::getStatus, StatusEnum.Normal)
                .orderByDesc(ApiConfig::getSort)
        ).stream().map(ApiConfigCascade::parseEntity).collect(Collectors.toList());
        // 循环整合父子分组级联
        List<ApiConfigCascade> resultList = CollectionUtil.newArrayList();
        apiConfigs.forEach(item -> {
            if (Constants.DEFAULT_PARENT_ID.equals(item.getParentId())) {
                resultList.add(item);
            } else {
                // 非父级用户，需要找到父类用户，并加入到子类去
                ApiConfigCascade parentApiConfig = apiConfigs.stream()
                        .filter(pItem -> item.getParentId().equals(pItem.getId()))
                        .findFirst().orElse(null);
                // 父类对象存在，则添加入子类
                if (ObjectUtil.isNotNull(parentApiConfig)) {
                    List<ApiConfigCascade> children = Optional.ofNullable(parentApiConfig.getChildren())
                            .orElse(CollectionUtil.newArrayList());
                    children.add(item);
                    parentApiConfig.setChildren(children);
                }

            }
        });
        return resultList;
    }
}
