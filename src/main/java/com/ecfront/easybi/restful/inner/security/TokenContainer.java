package com.ecfront.easybi.restful.inner.security;

import com.ecfront.easybi.restful.exchange.security.BaseAuthedInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Token 容器，用于保存Token与认证信息的关联
 */
public class TokenContainer {

    public static <E> E getAuthedInfo(String token) {
        if (TOKEN_CONTAINER.containsKey(token)) {
            if (new Date().getTime() <= TOKEN_CONTAINER.get(token).expireTime || 0 == TOKEN_CONTAINER.get(token).expireTime) {
                return (E) TOKEN_CONTAINER.get(token);
            } else {
                TOKEN_CONTAINER.remove(token);
            }
        }
        return null;
    }

    public static <E> String addAuthedInfo(E authedInfo) {
        String pk = ((BaseAuthedInfo) authedInfo).PK;
        //delete old info by PK
        if (AUTHED_INFO_CONTAINER.containsKey(pk)) {
            TOKEN_CONTAINER.remove(AUTHED_INFO_CONTAINER.get(pk));
        }
        String token = System.nanoTime() + "";
        TOKEN_CONTAINER.put(token, (BaseAuthedInfo) authedInfo);
        AUTHED_INFO_CONTAINER.put(pk, token);
        return token;
    }

    public static void removeAuthedInfo(String token) {
        BaseAuthedInfo authedInfo = getAuthedInfo(token);
        TOKEN_CONTAINER.remove(token);
        AUTHED_INFO_CONTAINER.remove(authedInfo.PK);
    }

    private static final Map<String, BaseAuthedInfo> TOKEN_CONTAINER = new HashMap<String, BaseAuthedInfo>();
    //删除用户对应的Token.key PK，value Token
    private static final Map<String, String> AUTHED_INFO_CONTAINER = new HashMap<String, String>();

}
