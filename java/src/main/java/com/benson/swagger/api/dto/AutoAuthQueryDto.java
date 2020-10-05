package com.benson.swagger.api.dto;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.benson.swagger.api.entity.AutoAuth;
import com.benson.swagger.api.util.CommonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 自动授权查询类
 *
 * @author zhangby
 * @date 15/9/20 10:42 am
 */
@Data
@Accessors(chain = true)
public class AutoAuthQueryDto {
    private String name;
    private String status;

    /** 条件查询 */
    public LambdaQueryWrapper<AutoAuth> getWrapper() {
        LambdaQueryWrapper<AutoAuth> queryWrapper = new LambdaQueryWrapper<AutoAuth>()
                .orderByDesc(AutoAuth::getSort);
        // 设置查询
        CommonUtil.notEmpty(name).ifPresent(item -> queryWrapper.like(AutoAuth::getName, item));
        CommonUtil.notEmpty(status).ifPresent(item -> queryWrapper.eq(AutoAuth::getStatus, item));
        return queryWrapper;
    }
}
