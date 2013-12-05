package com.ecfront.easybi.restful.exchange;

public class ResponseVO<T> {

    private int code;
    private String msg;
    private T body;

    public ResponseVO(int code, String msg, T body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }

    public ResponseVO(int code, T body) {
        this.code = code;
        this.body = body;
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

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
