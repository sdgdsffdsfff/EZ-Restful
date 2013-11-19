package com.ecfront.easybi.restful.exchange;

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
