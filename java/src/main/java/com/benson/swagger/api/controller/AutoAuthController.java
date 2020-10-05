package com.benson.swagger.api.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.benson.common.creator.entity.Pagination;
import com.benson.swagger.api.dto.AutoAuthQueryDto;
import com.benson.swagger.api.entity.AutoAuth;
import com.benson.swagger.api.entity.base.ResultPoJo;
import com.benson.swagger.api.service.IAutoAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 自动授权记录 前端控制器
 * </p>
 *
 * @author zhangby
 * @since 2020-09-14
 */
@RestController
@RequestMapping("/api/auto/auth")
@Api(tags = "自动授权管理 （ Automatic authorization management ）")
public class AutoAuthController {

    @Autowired
    IAutoAuthService autoAuthService;

    /**
     * 获取分页查询记录
     *
     * @return
     */
    @GetMapping("")
    @ApiOperation(value = "获取分页查询记录", notes = "获取分页查询记录", produces = "application/json")
    public ResultPoJo<IPage<AutoAuth>> getAutoAuthList(Pagination pagination, AutoAuthQueryDto autoAuthQueryDto) {
        IPage<AutoAuth> page = autoAuthService.page(pagination.page(), autoAuthQueryDto.getWrapper());
        return ResultPoJo.ok(page);
    }

    /**
     * 获取详情
     *
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取详情", notes = "获取详情", produces = "application/json")
    public ResultPoJo<AutoAuth> getAutoAuth(@PathVariable String id) {
        AutoAuth autoAuth = autoAuthService.getById(id);
        return ResultPoJo.ok(autoAuth);
    }

    /**
     * 保存对象
     *
     * @return
     */
    @PostMapping("")
    @ApiOperation(value = "保存对象", notes = "保存对象", produces = "application/json")
    public ResultPoJo saveAutoAuth(@Valid @RequestBody AutoAuth autoAuth) {
        autoAuth.preInsert();
        autoAuth.insert();
        return ResultPoJo.ok();
    }

    /**
     * 更新对象
     *
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "更新对象", notes = "更新对象", produces = "application/json")
    public ResultPoJo updateAutoAuth(@PathVariable String id, @Valid @RequestBody AutoAuth autoAuth) {
        autoAuth.setId(id).preUpdate();
        autoAuth.updateById();
        return ResultPoJo.ok();
    }

    /**
     * 删除对象
     *
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除对象", notes = "删除对象", produces = "application/json")
    public ResultPoJo deleteAutoAuth(@PathVariable String id) {
        autoAuthService.removeByIds(CollectionUtil.newArrayList(id.split(",")));
        return ResultPoJo.ok();
    }

     /**
      * 获取最大排序值
      * @return
      */
     @GetMapping("/get/max/sort")
     @ApiOperation(value = "获取最大排序值", notes = "获取最大排序值", produces = "application/json")
     public ResultPoJo<Integer> getMaxSort() {
         AutoAuth autoAuth = autoAuthService.getOne(new LambdaQueryWrapper<AutoAuth>()
                 .select(AutoAuth::getSort)
                 .orderByDesc(AutoAuth::getSort)
                 .last("limit 1")
         );
         Integer maxSort = Optional.ofNullable(autoAuth)
                 .map(AutoAuth::getSort)
                 .map(item -> item + 10)
                 .orElse(10);
         return ResultPoJo.ok(maxSort);
     }

      /**
       * 获取选择下拉框数据
       * @return
       */
      @GetMapping("/get/select/data")
      @ApiOperation(value = "获取选择下拉框数据", notes = "获取选择下拉框数据", produces = "application/json")
      public ResultPoJo<List<Dict>> getSelectData() {
          List<Dict> selectData = autoAuthService.list(new LambdaQueryWrapper<AutoAuth>()
                  .orderByDesc(AutoAuth::getSort)
          ).stream().map(item ->
                  Dict.create()
                          .set("label", item.getName())
                          .set("value", item.getId())
          ).collect(Collectors.toList());
          return ResultPoJo.ok(selectData);
      }
}

