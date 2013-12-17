package com.ecfront.easybi.restful.test;

import com.ecfront.easybi.restful.exchange.HttpMethod;
import com.ecfront.easybi.restful.exchange.security.BaseAuthedInfo;
import com.ecfront.easybi.restful.exchange.security.RestfulSecurityAdapter;

import java.util.Map;

public class CustomRestfulSecurityAdapter implements RestfulSecurityAdapter<BaseAuthedInfo> {


    @Override
    public BaseAuthedInfo login(Map<String, String> authInfo) {
        return new BaseAuthedInfo();
    }

    @Override
    public boolean logout(BaseAuthedInfo authedInfo) {
        return true;
    }

    @Override
    public boolean auth(HttpMethod httpMethod, String uri, String token) {
        return true;
    }
}
