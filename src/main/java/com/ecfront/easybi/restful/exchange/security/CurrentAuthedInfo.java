package com.ecfront.easybi.restful.exchange.security;

import com.ecfront.easybi.restful.inner.security.TokenContainer;

public class CurrentAuthedInfo {

    public static <E> E get() {
        return (E) CURRENT_AUTHED_INFO.get();
    }

    public static void set(String token) {
        CURRENT_AUTHED_INFO.set((BaseAuthedInfo) TokenContainer.getAuthedInfo(token));
    }

    private static final ThreadLocal<BaseAuthedInfo> CURRENT_AUTHED_INFO = new ThreadLocal<BaseAuthedInfo>();

}
