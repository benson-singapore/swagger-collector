package com.benson.swagger.api.service.impl;

import com.benson.swagger.api.entity.AutoAuth;
import com.benson.swagger.api.mapper.AutoAuthDao;
import com.benson.swagger.api.service.IAutoAuthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 自动授权记录 服务实现类
 * </p>
 *
 * @author zhangby
 * @since 2020-09-14
 */
@Service
public class AutoAuthServiceImpl extends ServiceImpl<AutoAuthDao, AutoAuth> implements IAutoAuthService {

}
