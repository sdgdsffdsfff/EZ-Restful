package com.ecfront.easybi.restful.inner.security;

import com.ecfront.easybi.restful.exchange.HttpMethod;
import com.ecfront.easybi.restful.exchange.ResponseVO;
import com.ecfront.easybi.restful.exchange.RestfulHelper;
import com.ecfront.easybi.restful.exchange.annotation.Allow;
import com.ecfront.easybi.restful.exchange.security.BaseAuthedInfo;
import com.ecfront.easybi.restful.exchange.security.CurrentAuthedInfo;
import com.ecfront.easybi.restful.exchange.security.RestfulSecurityAdapter;
import com.ecfront.easybi.restful.exchange.spring.SpringContextHolder;
import com.ecfront.easybi.restful.inner.ConfigContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 安全处理器
 */
public class RestfulSecurityProcessor {

    public ResponseVO loginOrlogout(String uri, Map<String, String[]> parameter, String token) {
        if (-1 != uri.indexOf(ConfigContainer.SECURITY_LOGIN)) {
            return login(parameter);
        } else if (-1 != uri.indexOf(ConfigContainer.SECURITY_LOGOUT)) {
            return logout(token);
        }
        return null;
    }

    private ResponseVO login(Map<String, String[]> parameter) {
        Map<String, String> authInfo = new HashMap<String, String>();
        for (String key : authInfo.keySet()) {
            if (!key.startsWith("__")) {
                authInfo.put(key, parameter.get(key)[0]);
            }
        }
        BaseAuthedInfo authedInfo = restfulSecurityAdapter.login(authInfo);
        if (null != authedInfo) {
            return RestfulHelper.success(TokenContainer.addAuthedInfo(authedInfo));
        }
        return RestfulHelper.unauthorized();
    }

    private ResponseVO logout(String token) {
        BaseAuthedInfo authedInfo = TokenContainer.getAuthedInfo(token);
        TokenContainer.removeAuthedInfo(token);
        boolean isLogout = restfulSecurityAdapter.logout(authedInfo);
        if (isLogout) {
            return RestfulHelper.success();
        } else {
            return RestfulHelper.unavailable();
        }
    }

    public boolean auth(HttpMethod httpMethod, String uri, Method method, String token) {
        if (method.isAnnotationPresent(Allow.class)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Allow method:{}", method.getName());
            }
            return true;
        }
        CurrentAuthedInfo.set(token);
        boolean isAuth = restfulSecurityAdapter.auth(httpMethod, uri, token);
        if (!isAuth) {
            if (logger.isWarnEnabled()) {
                logger.warn("Unauthorized,url:{},method:{}", uri, httpMethod.getCode());
            }
        }
        return isAuth;
    }

    private RestfulSecurityAdapter restfulSecurityAdapter;

    public static RestfulSecurityProcessor getInstance() {
        if (INSTANCE == null) {
            synchronized (RestfulSecurityProcessor.class) {
                INSTANCE = new RestfulSecurityProcessor();
            }
        }
        return INSTANCE;
    }

    private RestfulSecurityProcessor() {
        if (ConfigContainer.IS_SPRING_SUPPORT) {
            restfulSecurityAdapter = SpringContextHolder.getBean(RestfulSecurityAdapter.class);
        } else {
            if (null != ConfigContainer.SECURITY_CLASS && !"".equals(ConfigContainer.SECURITY_CLASS.trim())) {
                try {
                    restfulSecurityAdapter = (RestfulSecurityAdapter) Class.forName(ConfigContainer.SECURITY_CLASS.trim()).newInstance();
                } catch (Exception e) {
                    if (logger.isErrorEnabled()) {
                        logger.error("RestfulSecurityProcessor error:{}", e);
                    }
                }
            }
        }
    }

    private static volatile RestfulSecurityProcessor INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(RestfulSecurityProcessor.class);

}
