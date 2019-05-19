package com.real.skill.exception;

import com.real.skill.result.CodeMsg;
import com.real.skill.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 * 定义一个处理异常的方法
 *
 * @author: mabin
 * @create: 2019/5/16 11:18
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request,Exception e){

        e.printStackTrace();

        if (e instanceof GlobalException){
            GlobalException globalException = (GlobalException) e;
            return Result.error(globalException.getCodeMsg());
        }

        if (e instanceof BindException){
            BindException bindException = (BindException) e;
            List<ObjectError> errorList = bindException.getAllErrors();
            ObjectError error = errorList.get(0);
            String errMsg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(errMsg));
        }else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
