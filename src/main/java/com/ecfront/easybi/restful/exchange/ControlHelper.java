package com.ecfront.easybi.restful.exchange;

import java.util.Map;

public class ControlHelper {

    public static ResponseVO success() {
        return new ResponseVO(UniformCode.SUCCESS.getCode(), "Request Success.", null);
    }

    public static <T> ResponseVO success(T result) {
        return new ResponseVO(UniformCode.SUCCESS.getCode(), "Request Success.", result);
    }

    public static ResponseVO notFound() {
        return new ResponseVO(UniformCode.NOT_FOUND.getCode(), "The resource is not found!", null);
    }

    public static ResponseVO badRequest() {
        return new ResponseVO(UniformCode.BAD_REQUEST.getCode(), "The request is bad!", null);
    }

    public static ResponseVO unavailable() {
        return new ResponseVO(UniformCode.SERVICE_UNAVAILABLE.getCode(), "The service is unavailable!", null);
    }

    public static ResponseVO unauthorized() {
        return new ResponseVO(UniformCode.UNAUTHORIZED.getCode(), "The request is unauthorized!", null);
    }

    public static ResponseVO unknownError() {
        return new ResponseVO(UniformCode.UNKNOWN_ERROR.getCode(), "Unknown error!", null);
    }

    public static int[] parsePageByRequest(Map<String, String[]> parameter) {
        String pageNumber = parameter.containsKey(FLAG_PAGE_NUMBER) ? parameter.get(FLAG_PAGE_NUMBER)[0] : "1";
        String pageSize = parameter.containsKey(FLAG_PAGE_SIZE) ? parameter.get(FLAG_PAGE_SIZE)[0] : "10";
        return new int[]{Integer.valueOf(pageNumber), Integer.valueOf(pageSize)};
    }

    private static final String FLAG_PAGE_NUMBER = "pageNumber";
    private static final String FLAG_PAGE_SIZE = "pageSize";
}
