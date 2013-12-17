package com.ecfront.easybi.restful.inner;


import com.ecfront.easybi.base.utils.PropertyHelper;

public class ConfigContainer {

    public static final String METHOD_TYPE = "__method";
    public static final String TOKEN = "__token";

    public static final String FLAG_PAGE_NUMBER = "__pageNumber";
    public static final String FLAG_PAGE_SIZE = "__pageSize";

    public static String SCAN_BASE_PATH;
    public static String SECURITY_CLASS;
    public static String SECURITY_LOGIN;
    public static String SECURITY_LOGOUT;
    public static Boolean IS_SPRING_SUPPORT;
    public static String JSONP_CALLBACK;

    static {
        SCAN_BASE_PATH = PropertyHelper.get("ez_restful_scan_base_path");
        SECURITY_CLASS = PropertyHelper.get("ez_restful_security_class");
        SECURITY_LOGIN = PropertyHelper.get("ez_restful_security_login");
        SECURITY_LOGOUT = PropertyHelper.get("ez_restful_security_logout");
        String temp = PropertyHelper.get("ez_restful_spring_support");
        IS_SPRING_SUPPORT = null != temp && "true".equalsIgnoreCase(temp.trim()) ? true : false;
        JSONP_CALLBACK = PropertyHelper.get("ez_restful_jsonp_callback");
    }

}
