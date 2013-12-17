package com.ecfront.easybi.restful.inner;

/**
 * <h1>统一编码</h1>
 */
public enum UniformCode {

    SUCCESS(200),
    UNAUTHORIZED(401),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    UNKNOWN_ERROR(500),
    SERVICE_UNAVAILABLE(504);


    private final int code;


    private UniformCode(int code) {
        this.code = code;
    }

    /**
     * 返回Code
     *
     * @return
     */
    public int getCode() {
        return code;
    }

    /**
     * 返回Json格式的Code
     *
     * @return
     */
    public String getStatusCode() {
        return "{\"status\":" + code + "}";
    }
}
