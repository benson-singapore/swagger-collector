package com.benson.swagger.api.exception;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.benson.swagger.api.entity.base.ResultPoJo;
import com.benson.swagger.api.util.CommonUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 自定义异常拦截
 *
 * @author zhangby
 * @date 2018/11/5 4:19 PM
 */
@ControllerAdvice
@ResponseBody
public class MyBaseExceptionHandler {

    /**
     * logger
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 捕捉自定义的异常
     *
     * @param exception exception
     * @return ResponseEntity<ResultPoJo>
     */
    @ExceptionHandler(value = MyBaselogicException.class)
    public ResponseEntity<ResultPoJo> handleServiceException(MyBaselogicException exception) {
        logger.info("ERROR : " + JSONUtil.parseObj(CommonUtil.loadException2ResultPoJo(exception)).toString());
        logger.info("");
        return new ResponseEntity(
                CommonUtil.loadException2ResultPoJo(exception),
                HttpStatus.OK);
    }

    /**
     * 自定义参数异常拦截
     */
    @ExceptionHandler(value = { MethodArgumentNotValidException.class, BindException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResultPoJo> validException(HttpServletRequest request, Exception ex) {
        List<ObjectError> errors = Lists.newArrayList();
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException c = (MethodArgumentNotValidException) ex;
            errors =c.getBindingResult().getAllErrors();
        }
        if (ex instanceof BindException) {
            BindException c = (BindException) ex;
            errors =c.getBindingResult().getAllErrors();
        }
        StringJoiner stringJoiner = new StringJoiner(" , ", "{ ", " }");
        errors.forEach(error -> {
            List<String> rs = Stream.of(error.toString().split(";"))
                    .filter(mg -> mg.contains("default message"))
                    .map(mg -> Stream.of(mg.split(",")).filter(s -> s.contains("default message")).findFirst().orElse(""))
                    .collect(Collectors.toList());
            List<String> list = CommonUtil.splitStr4Temp(String.join("",rs), "default message [{}]");
            stringJoiner.add("[" + list.get(0) + "]" + list.get(1));
        });
        return  new ResponseEntity(
                ResultPoJo.error().setCode(HttpStatus.BAD_REQUEST.value() + "").setMsg("参数异常："+ stringJoiner.toString()),
                HttpStatus.OK);
    }

    /**
     * 捕获其他异常
     *
     * @param exception exception
     * @return ResponseEntity
     */
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResultPoJo> hadleServerException(Exception exception) {
        exception.printStackTrace();
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String msg = "服务错误，请稍后重试！";
        Class exceptionClazz = exception.getClass();
        if (Objects.equals(MissingServletRequestParameterException.class, exceptionClazz)) {
            msg = "缺少参数!";
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (Objects.equals(HttpRequestMethodNotSupportedException.class, exceptionClazz)) {
            httpStatus = HttpStatus.BAD_REQUEST;
            msg = exception.getMessage();
        } else if ("AccessDeniedException: 不允许访问".equals(ExceptionUtil.getMessage(exception))) {
            msg = "不允许访问!";
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else {
            msg = "服务错误，请稍后重试！（" + exception.getMessage() + "）";
        }

        return new ResponseEntity(
                ResultPoJo.error().setCode(httpStatus.value()+"").setMsg("参数异常："+ msg),
                httpStatus);
    }

}