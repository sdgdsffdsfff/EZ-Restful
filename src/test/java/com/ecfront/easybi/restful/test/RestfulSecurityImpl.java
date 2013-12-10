package com.ecfront.easybi.restful.test;

import com.ecfront.easybi.restful.exchange.HttpMethod;
import com.ecfront.easybi.restful.exchange.RestfulSecurity;

public class RestfulSecurityImpl implements RestfulSecurity {
    @Override
    public boolean auth(HttpMethod httpMethod, String uri, String token) {
        return true;
    }
}
