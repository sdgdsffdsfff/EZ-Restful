package com.ecfront.easybi.restful.exchange;


import com.ecfront.easybi.restful.inner.ConfigContainer;
import com.ecfront.easybi.restful.inner.security.CurrentAuthedInfo;
import org.apache.commons.fileupload.FileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <h1>辅助类</h1>
 */
public class RestfulHelper {

    public static <E> E getCurrentAuthedInfo(){
       return  CurrentAuthedInfo.get();
    }

    /**
     * 分页解析
     */
    public static int[] page(Map<String, String[]> parameter) {
        String pageNumber = parameter.containsKey(ConfigContainer.FLAG_PAGE_NUMBER) ? parameter.get(ConfigContainer.FLAG_PAGE_NUMBER)[0] : "1";
        String pageSize = parameter.containsKey(ConfigContainer.FLAG_PAGE_SIZE) ? parameter.get(ConfigContainer.FLAG_PAGE_SIZE)[0] : "10";
        return new int[]{Integer.valueOf(pageNumber), Integer.valueOf(pageSize)};
    }

    /**
     * 上传所有文件
     *
     * @param basePath  上传根目录
     * @param fileItems 要上传的文件
     * @return 上传文件路径
     */
    public static String[] uploadAll(String basePath, List<FileItem> fileItems) throws Exception {
        List<String> result = new ArrayList<String>();
        File file;
        for (FileItem item : fileItems) {
            file = new File(item.getName());
            item.write(new File(basePath, file.getName()));
            result.add(basePath + item.getName());
        }
        return result.toArray(new String[fileItems.size()]);
    }

    /**
     * 上传单个文件
     *
     * @param basePath  上传根目录
     * @param fileItems 要上传的文件
     * @return 上传文件路径
     */
    public static String uploadOne(String basePath, List<FileItem> fileItems) throws Exception {
        if (1 == fileItems.size()) {
            FileItem fileItem = fileItems.get(0);
            File file = new File(fileItem.getName());
            fileItem.write(new File(basePath, file.getName()));
            return basePath + fileItem.getName();
        }
        return null;
    }

    /**
     * 成功
     */
    public static ResponseVO success() {
        return new ResponseVO(com.ecfront.easybi.restful.exchange.UniformCode.SUCCESS.getCode(), "Request Success.", null);
    }

    /**
     * 成功，带对象
     *
     * @param result 返回对象
     */
    public static <T> ResponseVO success(T result) {
        return new ResponseVO(com.ecfront.easybi.restful.exchange.UniformCode.SUCCESS.getCode(), "Request Success.", result);
    }

    /**
     * 没有找到资源
     */
    public static ResponseVO notFound() {
        return new ResponseVO(com.ecfront.easybi.restful.exchange.UniformCode.NOT_FOUND.getCode(), "The resource is not found!", null);
    }

    /**
     * 没有找到资源
     */
    public static ResponseVO notFound(String message) {
        return new ResponseVO(com.ecfront.easybi.restful.exchange.UniformCode.NOT_FOUND.getCode(), message, null);
    }

    /**
     * 请求错误，如参数有问题
     */
    public static ResponseVO badRequest() {
        return new ResponseVO(com.ecfront.easybi.restful.exchange.UniformCode.BAD_REQUEST.getCode(), "The request is bad!", null);
    }

    /**
     * 服务不可用
     */
    public static ResponseVO unavailable() {
        return new ResponseVO(com.ecfront.easybi.restful.exchange.UniformCode.SERVICE_UNAVAILABLE.getCode(), "The service is unavailable!", null);
    }

    /**
     * 认证错误
     */
    public static ResponseVO unauthorized() {
        return new ResponseVO(com.ecfront.easybi.restful.exchange.UniformCode.UNAUTHORIZED.getCode(), "The request is unauthorized!", null);
    }

    /**
     * 未知错误
     */
    public static ResponseVO unknownError() {
        return new ResponseVO(com.ecfront.easybi.restful.exchange.UniformCode.UNKNOWN_ERROR.getCode(), "Unknown error!", null);
    }

}
