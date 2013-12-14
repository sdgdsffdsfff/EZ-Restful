package com.ecfront.easybi.restful.exchange;

import com.ecfront.easybi.restful.inner.ConfigContainer;

import java.util.Map;

/**
 * <h1>控制器辅助类</h1>
 */
public class ControlHelper {

    /**
     * 成功
     */
    public static ResponseVO success() {
        return new ResponseVO(UniformCode.SUCCESS.getCode(), "Request Success.", null);
    }

    /**
     * 成功，带对象
     *
     * @param result 返回对象
     */
    public static <T> ResponseVO success(T result) {
        return new ResponseVO(UniformCode.SUCCESS.getCode(), "Request Success.", result);
    }

    /**
     * 没有找到资源
     */
    public static ResponseVO notFound() {
        return new ResponseVO(UniformCode.NOT_FOUND.getCode(), "The resource is not found!", null);
    }

    /**
     * 请求错误，如参数有问题
     */
    public static ResponseVO badRequest() {
        return new ResponseVO(UniformCode.BAD_REQUEST.getCode(), "The request is bad!", null);
    }

    /**
     * 服务不可用
     */
    public static ResponseVO unavailable() {
        return new ResponseVO(UniformCode.SERVICE_UNAVAILABLE.getCode(), "The service is unavailable!", null);
    }

    /**
     * 认证错误
     */
    public static ResponseVO unauthorized() {
        return new ResponseVO(UniformCode.UNAUTHORIZED.getCode(), "The request is unauthorized!", null);
    }

    /**
     * 未知错误
     */
    public static ResponseVO unknownError() {
        return new ResponseVO(UniformCode.UNKNOWN_ERROR.getCode(), "Unknown error!", null);
    }

    /**
     * 分页解析
     */
    public static int[] parsePageByRequest(Map<String, String[]> parameter) {
        String pageNumber = parameter.containsKey(ConfigContainer.FLAG_PAGE_NUMBER) ? parameter.get(ConfigContainer.FLAG_PAGE_NUMBER)[0] : "1";
        String pageSize = parameter.containsKey(ConfigContainer.FLAG_PAGE_SIZE) ? parameter.get(ConfigContainer.FLAG_PAGE_SIZE)[0] : "10";
        return new int[]{Integer.valueOf(pageNumber), Integer.valueOf(pageSize)};
    }


}
