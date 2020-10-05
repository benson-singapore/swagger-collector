package com.benson.swagger.api.dto;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.benson.swagger.api.constants.Constants;
import com.benson.swagger.api.entity.ApiConfig;
import com.benson.swagger.api.util.CommonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询参数实体
 *
 * @author zhangby
 * @date 2/9/20 5:03 pm
 */
@Data
@Accessors(chain = true)
public class ApiConfigQueryDto {
    private String name;
    private String value;
    private String remarks;
    private String parentId;

    /**
     * 设置查询条件
     */
    public LambdaQueryWrapper<ApiConfig> queryWrapper() {
        LambdaQueryWrapper<ApiConfig> queryWrapper = new LambdaQueryWrapper<ApiConfig>()
                .orderByDesc(ApiConfig::getSort);
        // 设置查询条件
        CommonUtil.notEmpty(name).ifPresent(item -> queryWrapper.like(ApiConfig::getName, item));
        CommonUtil.notEmpty(value).ifPresent(item -> queryWrapper.like(ApiConfig::getValue, item));
        CommonUtil.notEmpty(remarks).ifPresent(item -> queryWrapper.like(ApiConfig::getRemarks, item));
        queryWrapper.eq(ApiConfig::getParentId, CommonUtil.notEmpty(parentId).orElse(Constants.DEFAULT_PARENT_ID));
        return queryWrapper;
    }
}
