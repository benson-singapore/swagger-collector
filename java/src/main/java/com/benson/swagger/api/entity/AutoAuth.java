package com.benson.swagger.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.benson.swagger.api.entity.base.BaseEntity;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.benson.swagger.api.entity.base.EnumFormat;
import com.benson.swagger.api.enums.MethodEnum;
import com.benson.swagger.api.enums.StatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 自动授权记录
 * </p>
 *
 * @author zhangby
 * @since 2020-09-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_auto_auth")
@ApiModel(value="AutoAuth对象", description="自动授权记录")
public class AutoAuth extends BaseEntity<AutoAuth> {

private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "请求地址")
    private String url;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @EnumFormat
    @ApiModelProperty(value = "请求方式： 1. get 2. post")
    private MethodEnum method;

    @EnumFormat
    @ApiModelProperty(value = "状态：0 正常 1 停用")
    private StatusEnum status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "token key 获取方式")
    private String tokenKey;

    @ApiModelProperty(value = "创建者",hidden = true)
    private String createBy;

    @ApiModelProperty(value = "创建时间",example = "2020-09-14 00:00:00",hidden = true)
    private Date createDate;

    @ApiModelProperty(value = "更新者",hidden = true)
    private String updateBy;

    @ApiModelProperty(value = "更新时间",example = "2020-09-14 00:00:00",hidden = true)
    private Date updateDate;

    @ApiModelProperty(value = "备注信息")
    private String remarks;

    @TableLogic
    @ApiModelProperty(value = "删除标记",example = "0",hidden = true)
    private String delFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
