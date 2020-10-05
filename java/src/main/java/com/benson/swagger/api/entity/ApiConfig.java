package com.benson.swagger.api.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.benson.swagger.api.entity.base.BaseEntity;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.benson.swagger.api.entity.base.EnumFormat;
import com.benson.swagger.api.enums.StatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * API 系统配置表
 * </p>
 *
 * @author zhangby
 * @since 2020-09-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sys_api_config")
@ApiModel(value="ApiConfig对象", description="API 系统配置表")
public class ApiConfig extends BaseEntity<ApiConfig> {

private static final long serialVersionUID=1L;

    private String id;

    @ApiModelProperty(value = "父类id")
    private String parentId;

    @ApiModelProperty(value = "授权ID")
    private String autoAuthId;

    @NotBlank(message = "Name 不能为空")
    @ApiModelProperty(value = "名称")
    private String name;

    @NotBlank(message = "Value 不能为空")
    @ApiModelProperty(value = "值")
    private String value;
    private String host;
    @EnumFormat
    @ApiModelProperty(value = "状态：0 正常 1 停用")
    private StatusEnum status;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "创建者",hidden = true)
    private String createBy;

    @ApiModelProperty(value = "创建时间",example = "2020-09-02 00:00:00",hidden = true)
    private Date createDate;

    @ApiModelProperty(value = "更新者",hidden = true)
    private String updateBy;

    @ApiModelProperty(value = "更新时间",example = "2020-09-02 00:00:00",hidden = true)
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
