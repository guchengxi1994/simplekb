package org.xiaoshuyui.simplekb;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.xiaoshuyui.simplekb.common.response.Result;

//@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 处理所有未捕获的异常
    @ExceptionHandler(Exception.class)
    public Result handleAllExceptions(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        return Result.error("服务器内部错误");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.error(e.getMessage());
        return Result.error("访问失败");
    }

    // 处理参数校验错误
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        return Result.error(errorMessage);
    }

    // 处理绑定异常 (例如表单提交错误)
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException ex) {
        log.error(ex.getMessage());
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return Result.error(errorMessage);
    }
}
