package com.ecfront.easybi.restful.exchange;

/**
 * <h1>返回VO</h1>
 * <p>所有返回的信息都通过此VO包装。</p>
 */
public class ResponseVO {
    private int code;
    private String msg;
    private Object body;

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

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public ResponseVO() {
    }

    public ResponseVO(int code) {
        this.code = code;
    }

    public ResponseVO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseVO(int code, Object body) {
        this.code = code;
        this.body = body;
    }

    public ResponseVO(int code, String msg, Object body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }
}
