package com.ecfront.easybi.restful.exchange.security;

import com.ecfront.easybi.restful.exchange.HttpMethod;

import java.util.Map;

/**
 * 安全处理接口
 */
public interface RestfulSecurityAdapter<E extends BaseAuthedInfo> {

    E login(Map<String, String> authInfo);

    boolean logout(E authedInfo);

    boolean auth(HttpMethod httpMethod, String uri, String token);

}
