package com.duyi.readingweb.exceptionHandler;

import com.duyi.readingweb.bean.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //指定出现什么异常处理这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody//为了返回数据
    public ResultMsg error(Exception e){
        log.error("当前异常信息为：{}",e);
        return ResultMsg.error().message("执行了全局异常处理...");
    }
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public ResultMsg error(ArithmeticException e){
        log.error("当前异常信息为：{}",e);
        return ResultMsg.error().message("执行了ArithmeticException异常处理...");
    }

    //自定义异常,想要捕获，需要在catch中 主动抛出
    @ExceptionHandler(UserDefinedException.class)
    @ResponseBody
    public ResultMsg error(UserDefinedException e){
        log.error("当前异常信息为：{}",e);
        return ResultMsg.error().code(e.getCode()).message(e.getMsg());
    }
}
