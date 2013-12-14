package com.ecfront.easybi.restful.inner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ecfront.easybi.restful.exchange.ControlHelper;
import com.ecfront.easybi.restful.exchange.HttpMethod;
import com.ecfront.easybi.restful.exchange.ResponseVO;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <h1>请求解析执行类</h1>
 * <p>用于解析请求内容并执行对应的业务方法返回结果。</p>
 */
public class RestfulExecutor {

    public ResponseVO execute(HttpMethod httpMethod, String uri, Object model, Map<String, String[]> parameter, List<FileItem> fileItems) throws InvocationTargetException, IllegalAccessException {
        Object[] restfulResult = PathChainContainer.getInstance().parsePath(httpMethod, uri);
        if (restfulResult != null) {
            Object reflectObject = restfulResult[0];
            Method reflectMethod = (Method) restfulResult[1];
            List<Object> invokeArgs = new ArrayList<Object>();
            if (packageInvokeArgs(reflectMethod, (List<String>) restfulResult[2], parameter, model, fileItems, invokeArgs)) {
                return ControlHelper.success(reflectMethod.invoke(reflectObject, invokeArgs.toArray()));
            } else {
                return ControlHelper.badRequest();
            }
        }
        return ControlHelper.notFound();
    }

    /**
     * 解析并组装方法参数
     *
     * @param reflectMethod     反射得到的方法
     * @param urlParameters     Restful中通过URL解析出来的参数列表
     * @param requestParameters Request中传入的参数列表
     * @param model             Request中的模型
     * @param fileItems         上传文件列表
     * @param invokeArgs        需要返回的参数列表
     * @return 是否成功解析
     */
    private boolean packageInvokeArgs(Method reflectMethod, List<String> urlParameters, Map<String, String[]> requestParameters, Object model, List<FileItem> fileItems, List<Object> invokeArgs) {
        Type parameterType;
        Type[] reflectGenericParameterTypes = reflectMethod.getGenericParameterTypes();
        //把URL解析出来的参数添加到invokeArgs中
        for (int i = 0; i < reflectGenericParameterTypes.length && i < urlParameters.size(); i++) {
            parameterType = reflectGenericParameterTypes[i];
            if (String.class.equals(parameterType)) {
                invokeArgs.add(urlParameters.get(i));
            } else if (Integer.class
                    .equals(parameterType)
                    || int.class
                    .equals(parameterType)) {
                invokeArgs.add(Integer.valueOf(urlParameters.get(i)));
            } else if (Long.class
                    .equals(parameterType)
                    || long.class
                    .equals(parameterType)) {
                invokeArgs.add(Long.valueOf(urlParameters.get(i)));
            } else if (Double.class
                    .equals(parameterType)
                    || double.class
                    .equals(parameterType)) {
                invokeArgs.add(Double.valueOf(urlParameters.get(i)));
            } else if (Float.class
                    .equals(parameterType)
                    || float.class
                    .equals(parameterType)) {
                invokeArgs.add(Float.valueOf(urlParameters.get(i)));
            } else if (BigDecimal.class
                    .equals(parameterType)) {
                invokeArgs.add(new BigDecimal(urlParameters.get(i)));
            } else if (UUID.class
                    .equals(parameterType)) {
                invokeArgs.add(UUID.fromString(urlParameters.get(i)));
            } else {
                if (logger.isWarnEnabled()) {
                    logger.warn("The parameter type of " + parameterType + " is not registered.");
                }
                return false;
            }
        }
        if (reflectGenericParameterTypes.length > urlParameters.size()) {
            for (int i = urlParameters.size(); i < reflectGenericParameterTypes.length; i++) {
                parameterType = reflectGenericParameterTypes[i];
                if (parameterType instanceof ParameterizedTypeImpl && ((ParameterizedTypeImpl) parameterType).getRawType().equals(Map.class)) {
                    //Add parameter , type of Map<String,String[]>
                    invokeArgs.add(requestParameters);
                } else if (parameterType instanceof ParameterizedType
                        && null != ((ParameterizedType) parameterType).getActualTypeArguments()
                        && 1 == ((ParameterizedType) parameterType).getActualTypeArguments().length
                        && FileItem.class.equals(((ParameterizedType) parameterType).getActualTypeArguments()[0])) {
                    //Add file items
                    invokeArgs.add(fileItems);
                } else if (model != null && parameterType instanceof Class) {
                    //Add model
                    if (model instanceof JSONObject) {
                        invokeArgs.add(JSON.toJavaObject((JSONObject) model, (Class) parameterType));
                    } else {
                        invokeArgs.add(model);
                    }
                } else {
                    if (logger.isWarnEnabled()) {
                        logger.warn("The parameter type of " + parameterType + " is not registered.");
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static RestfulExecutor getInstance() {
        if (INSTANCE == null) {
            synchronized (RestfulExecutor.class) {
                INSTANCE = new RestfulExecutor();
            }
        }
        return INSTANCE;
    }

    private RestfulExecutor() {
    }

    private static volatile RestfulExecutor INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(RestfulExecutor.class);


}
