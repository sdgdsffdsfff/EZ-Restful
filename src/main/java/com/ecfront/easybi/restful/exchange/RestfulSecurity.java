package com.ecfront.easybi.restful.exchange;

/**
 * 安全处理接口
 */
public interface RestfulSecurity {

    boolean auth(HttpMethod httpMethod, String uri, String token);

}
