package org.xiaoshuyui.simplekb;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.xiaoshuyui.simplekb.common.Result;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 处理所有未捕获的异常
    @ExceptionHandler(Exception.class)
    public Result handleAllExceptions(Exception ex, WebRequest request) {
        return Result.error("服务器内部错误");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        return Result.error("访问失败");
    }

    // 处理参数校验错误
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return Result.error(errorMessage);
    }

    // 处理绑定异常 (例如表单提交错误)
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.error(errorMessage);
    }
}
