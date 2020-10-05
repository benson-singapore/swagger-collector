package com.benson.swagger.api.entity.base;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.benson.swagger.api.constants.Constants;
import com.benson.swagger.api.util.CommonUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 返回数据
 *
 * @author zhangby
 * @date 2019-05-13 12:11
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ResultPoJo", description = "返回数据")
public class ResultPoJo<T> {

    @ApiModelProperty(value = "返回码：000【正确】，其他错误", example = "000")
    private String code;
    @ApiModelProperty(value = "返回信息", example = "成功！")
    private String msg;
    @ApiModelProperty(value = "返回结果", example = "{}")
    private T result;

    private ResultPoJo() {
        this.code = Constants.NORMAL;
    }

    /**
     * 链式构造
     *
     * @return ResultPoJo
     */
    public static ResultPoJo ok() {
        ResultPoJo poJo = new ResultPoJo();
        poJo.code = Constants.NORMAL;
        poJo.msg = Constants.MSG_000;
        return poJo;
    }

    /**
     * 链式构造
     *
     * @return ResultPoJo
     */
    public static ResultPoJo error() {
        ResultPoJo poJo = new ResultPoJo();
        poJo.code = Constants.ERROR;
        poJo.msg = Constants.MSG_999;
        return poJo;
    }

    /**
     * 创建成功返回结果
     */
    public static ResultPoJo ok(String s, Object rs) {
        return ResultPoJo.ok().setMsg(s).setResult(rs);
    }

    public static ResultPoJo ok(Object s) {
        return ResultPoJo.ok().setResult(s);
    }

    /**
     * 创建失败返回结果
     */
    public static ResultPoJo error(String num, Object... rs) {
        return CommonUtil.loadException2ResultPoJo(num, rs);
    }

    public String toJson() {
        return JSONUtil.parseObj(this).toString();
    }

    /**
     * 数据转换
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T toBean(Class<T> clazz) {
        T convert = null;
        try {
            convert = Convert.convert(clazz, getResult());
        } catch (Exception e) {
            convert = JSONUtil.parseObj(this).toBean(clazz);
        }
        return convert;
    }


}
