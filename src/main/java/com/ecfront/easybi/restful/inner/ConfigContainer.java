package com.ecfront.easybi.restful.inner;


import com.ecfront.easybi.base.utils.PropertyHelper;

public class ConfigContainer {

    public static final String METHOD_TYPE = "__method";
    public static final String TOKEN = "__token";

    public static final String FLAG_PAGE_NUMBER = "pageNumber";
    public static final String FLAG_PAGE_SIZE = "pageSize";

    public static String SCAN_BASE_PATH;
    public static String SECURITY;
    public static Boolean IS_SPRING_SUPPORT;
    public static String JSONP_CALLBACK;

    static {
        SCAN_BASE_PATH = PropertyHelper.get("ez_restful_scan_base_path");
        SECURITY = PropertyHelper.get("ez_restful_security");
        String temp = PropertyHelper.get("ez_restful_spring_support");
        IS_SPRING_SUPPORT = null != temp && "true".equalsIgnoreCase(temp.trim()) ? true : false;
        JSONP_CALLBACK = PropertyHelper.get("ez_restful_jsonp_callback");
    }

}
