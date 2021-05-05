package com.spc.feign.Controller;

import java.io.Serializable;

public class JsonResult<T> implements Serializable {
    private static final long serialVersionUID = 9211889136173018364L;

    /**
     * 状态码
     */
    private int code ;

    /**
     * 返回信息
     */
    private String msg ;

    /**
     * 响应内容，默认为null
     */
    private T data ;

    public JsonResult() {
    }

    /**
     * 默认成功
     */
    public JsonResult(T data) {
        this.code=200;
        this.msg="success";
        this.data=data;
    }

    /**
     * 指定状态码和提示信息
     * @param code
     * @param msg
     */
    public JsonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
