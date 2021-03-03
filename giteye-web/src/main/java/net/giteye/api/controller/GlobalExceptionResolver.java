package net.giteye.api.controller;

import net.giteye.api.resp.ApiResp;
import net.giteye.exception.GeErrorCode;
import net.giteye.exception.GeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author Bryan.Zhang
 * @since 2021/1/26
 */

@RestControllerAdvice(basePackages = "net.giteye.api.controller")
public class GlobalExceptionResolver {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = {GeException.class})
    public ApiResp<Object> handlerGeException(GeException e) {
        log.error(e.getMessage(),e);
        return ApiResp.failed(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    public ApiResp<Object> handlerException(Exception e) {
        log.error(e.getMessage(),e);
        return ApiResp.failed(GeErrorCode.UNKNOWN_EXCEPTION.getErrorCode(), e.getMessage());
    }
}
