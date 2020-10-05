package com.benson.swagger.api.exception;

/**
 * 自定义异常
 *
 * @author zhangby
 * @date 2017/11/30 下午7:10
 */
public class MyBaselogicException extends RuntimeException {
    private static final long serialVersionUID = -6317037305924958356L;
    /**
     * 错误代码
     */
    private String num;
    /**
     * msg参数
     */
    private Object[] msg;

    public MyBaselogicException(String num) {
        this.num = num;
    }

    public MyBaselogicException(String num, Object... msg) {
        this.num = num;
        this.msg = msg;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Object[] getMsg() {
        return msg;
    }

    public void setMsg(Object[] msg) {
        this.msg = msg;
    }
}