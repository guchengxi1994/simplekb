package org.xiaoshuyui.simplekb.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "接口返回对象", description = "接口返回对象")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    @Schema(name = "success")
    private boolean success = true;

    /**
     * 返回处理消息
     */
    @Schema(name = "message")
    private String message = "操作成功！";

    /**
     * 返回代码
     */
    @Schema(name = "code")
    private Integer code = 0;

    /**
     * 返回数据对象 data
     */
    @Schema(name = "result")
    private T result;

    /**
     * 返回数据对象 data
     */
    @Schema(name = "data")
    private T data;

    /**
     * 时间戳
     */
    @Schema(name = "timestamp")
    private long timestamp = System.currentTimeMillis();
    @JsonIgnore
    private String onlTable;

    public Result() {
    }

    @Deprecated
    public static Result<Object> ok() {
        Result<Object> r = new Result<Object>();
        r.setSuccess(true);
        r.setCode(CommonConstants.SC_OK_200);
        r.setMessage("成功");
        return r;
    }

    @Deprecated
    public static Result<Object> ok(String msg) {
        Result<Object> r = new Result<Object>();
        r.setSuccess(true);
        r.setCode(CommonConstants.SC_OK_200);
        r.setMessage(msg);
        return r;
    }

    @Deprecated
    public static Result<Object> ok(Object data) {
        Result<Object> r = new Result<Object>();
        r.setSuccess(true);
        r.setCode(CommonConstants.SC_OK_200);
        r.setResult(data);
        return r;
    }

    public static <T> Result<T> OK() {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(CommonConstants.SC_OK_200);
        r.setMessage("成功");
        return r;
    }

    public static <T> Result<T> OK(T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(CommonConstants.SC_OK_200);
        r.setResult(data);
        return r;
    }

    public static <T> Result<T> OK_data(T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(CommonConstants.SC_OK_200);
        //		r.setResult(data);
        r.setData(data);
        return r;
    }

    public static <T> Result<T> OK(String msg, T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(CommonConstants.SC_OK_200);
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

    public static Result<Object> error(String msg) {
        return error(CommonConstants.SC_INTERNAL_SERVER_ERROR_500, msg);
    }

    public static Result<Object> error(int code, String msg) {
        Result<Object> r = new Result<Object>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }

    public Result<T> success(String message) {
        this.message = message;
        this.code = CommonConstants.SC_OK_200;
        this.success = true;
        return this;
    }

    public Result<T> error500(String message) {
        this.message = message;
        this.code = CommonConstants.SC_INTERNAL_SERVER_ERROR_500;
        this.success = false;
        return this;
    }
}
