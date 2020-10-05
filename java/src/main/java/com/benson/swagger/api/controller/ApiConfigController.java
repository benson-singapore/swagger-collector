package com.benson.swagger.api.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.benson.common.creator.entity.Pagination;
import com.benson.common.creator.entity.ResultPoJo;
import com.benson.swagger.api.constants.Constants;
import com.benson.swagger.api.dto.ApiConfigCascade;
import com.benson.swagger.api.dto.ApiConfigQueryDto;
import com.benson.swagger.api.entity.ApiConfig;
import com.benson.swagger.api.service.IApiConfigService;
import com.benson.swagger.api.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangby
 * @since 2020-08-27
 */
@RestController
@RequestMapping("/api/config")
@Api(tags = "接口配置管理 ( Interface configuration management )")
public class ApiConfigController {

    @Autowired
    IApiConfigService apiConfigService;

    /**
     * 获取API配置列表
     *
     * @return
     */
    @GetMapping("")
    @ApiOperation(value = "获取API配置列表", notes = "获取API配置列表", produces = "application/json")
    public ResultPoJo<IPage<ApiConfig>> getApiConfigList(Pagination pagination, ApiConfigQueryDto apiConfigQueryDto) {
        LambdaQueryWrapper<ApiConfig> queryWrapper = apiConfigQueryDto.queryWrapper();
        IPage page = apiConfigService.page(pagination.page(), queryWrapper);
        return ResultPoJo.ok(page);
    }

    /**
     * 获取API配置详情
     *
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取API配置详情", notes = "获取API配置详情", produces = "application/json")
    public ResultPoJo<ApiConfig> getApiConfigDetail(@PathVariable String id) {
        return ResultPoJo.ok(apiConfigService.getById(id));
    }

    /**
     * 添加API配置记录
     *
     * @return
     */
    @PostMapping("")
    @ApiOperation(value = "添加API配置记录", notes = "添加API配置记录", produces = "application/json")
    public ResultPoJo saveApiConfig(@Valid @RequestBody ApiConfig apiConfig) {
        apiConfigService.saveApiConfig(apiConfig);
        return ResultPoJo.ok();
    }

    /**
     * 更新API配置记录
     *
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "更新API配置记录", notes = "更新API配置记录", produces = "application/json")
    public ResultPoJo updateApiConfig(@Valid @PathVariable String id, @RequestBody ApiConfig apiConfig) {
        apiConfigService.updateApiConfig(id, apiConfig);
        return ResultPoJo.ok();
    }

    /**
     * 删除API配置信息
     *
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除API配置信息", notes = "删除API配置信息", produces = "application/json")
    public ResultPoJo deleteApiConfig(@PathVariable String id) {
        apiConfigService.removeByIds(CollectionUtil.newArrayList(id.split(",")));
        return ResultPoJo.ok();
    }

    /**
     * 获取最大排序值
     *
     * @return
     */
    @GetMapping("/get/max/sort")
    @ApiOperation(value = "获取最大排序值", notes = "获取最大排序值", produces = "application/json")
    public ResultPoJo<Integer> getMaxSort(@RequestParam(value = "parentId", required = false) String parentId) {
        Integer maxSort = apiConfigService.getMaxSort(parentId);
        return ResultPoJo.ok(maxSort + 10);
    }

    /**
     * 级联查询 API配置级联
     *
     * @return
     */
    @GetMapping("/get/cascade")
    @ApiOperation(value = "级联查询 API配置级联", notes = "级联查询 API配置级联", produces = "application/json")
    public ResultPoJo<List<ApiConfigCascade>> getApiConfigCascade() {
        List<ApiConfigCascade> list = apiConfigService.getApiConfigCascade();
        return ResultPoJo.ok(list);
    }
}

