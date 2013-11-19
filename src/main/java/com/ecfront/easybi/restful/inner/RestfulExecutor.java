package com.ecfront.easybi.restful.inner;

import com.ecfront.easybi.restful.exchange.HttpMethod;
import com.ecfront.easybi.restful.exchange.ResponseVO;
import com.ecfront.easybi.restful.exchange.annotation.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <h1>请求解析执行类</h1>
 * <p>用于解析请求内容并执行对应的业务方法返回结果。</p>
 */
public class RestfulExecutor {

    public ResponseVO excute(HttpMethod httpMethod, String uri, Object model, Map<String, Object[]> parameter, InputStream inputStream) throws InvocationTargetException, IllegalAccessException {
        Object[] restfulResult = PathChainContainer.getInstance().parsePath(httpMethod, uri);
        if (restfulResult != null) {
            Object reflectObject = restfulResult[0];
            Method reflectMethod = (Method) restfulResult[1];
            List<Object> invokeArgs = new ArrayList<>();
            if (packageInvokeArgs(reflectMethod.getParameterTypes(), (List<String>) restfulResult[2], parameter, model, invokeArgs)) {
                return new ResponseVO(UniformCode.SUCCESS.getCode(), reflectMethod.invoke(reflectObject, invokeArgs.toArray()));
            } else {
                return new ResponseVO(UniformCode.BAD_REQUEST.getCode());
            }
        }
        return new ResponseVO(UniformCode.UNKNOWN_ERROR.getCode());
    }

    /**
     * 解析并组装方法参数
     *
     * @param reflectParameterTypes 反射得到的方法参数列表
     * @param urlParameters         Restful中通过URL解析出来的参数列表
     * @param requestParameters     Request中传入的参数列表
     * @param model                 Request中的模型
     * @param invokeArgs            需要返回的参数列表
     * @return 是否成功解析
     */
    private boolean packageInvokeArgs(Class<?>[] reflectParameterTypes, List<String> urlParameters, Map<String, Object[]> requestParameters, Object model, List<Object> invokeArgs) {
        Class<?> parameterType;
        //把URL解析出来的参数添加到invokeArgs中
        for (int i = 0; i < reflectParameterTypes.length && i < urlParameters.size(); i++) {
            parameterType = reflectParameterTypes[i];
            if (String.class.isAssignableFrom(parameterType)) {
                invokeArgs.add(urlParameters.get(i));
            } else if (Integer.class
                    .isAssignableFrom(parameterType)
                    || int.class
                    .isAssignableFrom(parameterType)) {
                invokeArgs.add(Integer.valueOf(urlParameters.get(i)));
            } else if (Long.class
                    .isAssignableFrom(parameterType)
                    || long.class
                    .isAssignableFrom(parameterType)) {
                invokeArgs.add(Long.valueOf(urlParameters.get(i)));
            } else if (Double.class
                    .isAssignableFrom(parameterType)
                    || double.class
                    .isAssignableFrom(parameterType)) {
                invokeArgs.add(Double.valueOf(urlParameters.get(i)));
            } else if (Float.class
                    .isAssignableFrom(parameterType)
                    || float.class
                    .isAssignableFrom(parameterType)) {
                invokeArgs.add(Float.valueOf(urlParameters.get(i)));
            } else if (BigDecimal.class
                    .isAssignableFrom(parameterType)) {
                invokeArgs.add(new BigDecimal(urlParameters.get(i)));
            } else {
                if (logger.isWarnEnabled()) {
                    logger.warn("The parameter type of " + parameterType.getName() + " is not registered.");
                }
                return false;
            }
        }
        if (reflectParameterTypes.length > urlParameters.size()) {
            for (int i = urlParameters.size(); i < reflectParameterTypes.length; i++) {
                parameterType = reflectParameterTypes[i];
                if (parameterType.equals(Map.class)) {
                    invokeArgs.add(requestParameters);
                } else if (model != null && parameterType.isAnnotationPresent(Model.class)) {
                    invokeArgs.add(model);
                } else {
                    if (logger.isWarnEnabled()) {
                        logger.warn("The parameter type of " + parameterType.getName() + " is not registered.");
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
