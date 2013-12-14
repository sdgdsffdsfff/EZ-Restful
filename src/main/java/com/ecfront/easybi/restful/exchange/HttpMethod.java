package com.ecfront.easybi.restful.exchange;

/**
 * <h1>方法类型</h1>
 */
public enum HttpMethod {
    POST("POST"), GET("GET"), DELETE("DELETE"), PUT("PUT");

    private String code;

    private HttpMethod(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
