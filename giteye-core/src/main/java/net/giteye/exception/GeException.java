package net.giteye.exception;

import cn.hutool.core.util.StrUtil;

/**
 * giteye统一包装异常类
 *
 * @author Bryan.Zhang
 * @since 1.0.0
 */
public class GeException extends RuntimeException{

    private String errorCode;

    public GeException(GeErrorCode errorCode, Throwable cause) {
        this(errorCode, errorCode.getErrorCode(), cause);
    }

    public GeException(GeErrorCode exceptionCode) {
        this(exceptionCode.getErrorCode(), exceptionCode.getMessage());
    }

    public GeException(GeErrorCode exceptionCode, String msg) {
        this(exceptionCode.getErrorCode(), StrUtil.format("[{}]{}",exceptionCode.getMessage(),msg));
    }

    public GeException(GeErrorCode exceptionCode, String msg, Throwable cause) {
        this(exceptionCode.getErrorCode(), StrUtil.format("[{}]{}",exceptionCode.getMessage(),msg), cause);
    }

    public GeException(String errorCode, String msg) {
        super(StrUtil.format("[{}]{}",errorCode,msg));
        this.errorCode = errorCode;
    }

    public GeException(String errorCode, String msg, Throwable cause) {
        super(StrUtil.format("[{}]{}",errorCode,msg), cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
