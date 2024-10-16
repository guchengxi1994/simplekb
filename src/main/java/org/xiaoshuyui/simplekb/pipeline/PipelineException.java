package org.xiaoshuyui.simplekb.pipeline;

/**
 * PipelineException类是RuntimeException的衍生类，用于处理管道执行过程中的异常情况
 * 该类的主要作用是封装在管道处理流程中可能出现的错误信息，并提供给调用者明确的错误描述
 */
public class PipelineException extends RuntimeException {
    /**
     * 构造函数，用于创建一个带有指定错误信息的PipelineException对象
     *
     * @param error 异常的详细信息，通常是一个描述错误原因或情况的字符串
     */
    public PipelineException(String error) {
        super(error);
    }
}

