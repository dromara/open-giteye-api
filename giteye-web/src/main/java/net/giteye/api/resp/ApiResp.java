package net.giteye.api.resp;

import net.giteye.exception.GeErrorCode;

/**
 * Giteye API响应返回数据
 *
 * @param <T> Api返回的业务数据类型
 * @author gongjun[dt_flys@hotmail.com]
 */
public class ApiResp<T> {

    /**
     * Api返回的业务数据
     */
    private T data;

    /**
     * Api响应结果状态
     */
    private final String status;

    /**
     * Api响应错误码
     */
    private String errorCode;

    /**
     * Api响应错误消息
     */
    private String errorMsg;

    public ApiResp(String status) {
        this.status = status;
    }

    /**
     * 创建成功的Api响应结果
     *
     * @param data Api返回的业务数据
     * @param <T>  Api返回的业务数据类型
     * @return {@link ApiResp}类实例
     */
    public static <T> ApiResp<T> success(T data) {
        ApiResp<T> resp = new ApiResp<>(ApiStatus.SUCCESS.getCode());
        resp.setData(data);
        return resp;
    }

    /**
     * 创建成功的Api响应结果
     *
     * @return {@link ApiResp}类实例
     */
    public static <T> ApiResp<T> success() {
        return new ApiResp<>(ApiStatus.SUCCESS.getCode());
    }

    /**
     * 创建失败的Api响应结果
     *
     * @param errorCode Api响应的消息
     * @return {@link ApiResp}类实例
     */
    public static ApiResp<Object> failed(String errorCode, String errorMsg) {
        ApiResp<Object> resp = new ApiResp<>(ApiStatus.FAILED.getCode());
        resp.setErrorCode(errorCode);
        resp.setErrorMsg(errorMsg);
        return resp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
