package com.benson.swagger.api.mapper;

import com.benson.swagger.api.entity.AutoAuth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 自动授权记录 Mapper 接口
 * </p>
 *
 * @author zhangby
 * @since 2020-09-14
 */
@Repository
public interface AutoAuthDao extends BaseMapper<AutoAuth> {

}
