package com.ecfront.easybi.restful.inner;


import com.ecfront.easybi.base.utils.PropertyHelper;
import com.ecfront.easybi.classscanner.exchange.ClassScanner;
import com.ecfront.easybi.restful.exchange.HttpMethod;
import com.ecfront.easybi.restful.exchange.annotation.*;
import com.ecfront.easybi.restful.exchange.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * <h1>Controller管理类</h1>
 */
public class ControllerManager {

    /**
     * 扫描并添加带@Uri的类
     * 此方法引用EZ.ClassScanner工具
     *
     * @param basePath 要扫描的根包
     * @link:https://github.com/gudaoxuri/EZ.ClassScanner
     */
    public void scanController(String basePath) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Scaning... basePath:{}", basePath);
        }
        Set<Class<?>> classes = ClassScanner.scan(basePath, Uri.class);
        if (classes != null && classes.size() > 0) {
            for (Class<?> clazz : classes) {
                addController(clazz);
            }
        }
    }

    /**
     * 添加Controller
     *
     * @param clazz 要添加的类
     */
    public void addController(Class<?> clazz) throws IllegalAccessException, InstantiationException {
        if (logger.isDebugEnabled()) {
            logger.debug("Find & Add Class:{}", clazz.getSimpleName());
        }
        Object object;
        if (isSpringSupport) {
            object = SpringContextHolder.getBean(clazz);
        } else {
            object = clazz.newInstance();
        }
        String uri = clazz.getAnnotation(Uri.class).value();
        Method[] methods = clazz.getMethods();
        Annotation[] annotations;
        for (Method method : methods) {
            annotations = method.getDeclaredAnnotations();
            if (annotations != null && annotations.length > 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Find & Add Method:{}", method.getName());
                }
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Get) {
                        PathChainContainer.getInstance().addPaths(HttpMethod.GET, uri + ((Get) annotation).value(), object, method);
                    } else if (annotation instanceof Post) {
                        PathChainContainer.getInstance().addPaths(HttpMethod.POST, uri + ((Post) annotation).value(), object, method);
                    } else if (annotation instanceof Put) {
                        PathChainContainer.getInstance().addPaths(HttpMethod.PUT, uri + ((Put) annotation).value(), object, method);
                    } else if (annotation instanceof Delete) {
                        PathChainContainer.getInstance().addPaths(HttpMethod.DELETE, uri + ((Delete) annotation).value(), object, method);
                    }
                }
            }
        }
    }

    public static ControllerManager getInstance() {
        if (INSTANCE == null) {
            synchronized (ControllerManager.class) {
                INSTANCE = new ControllerManager();
            }
        }
        return INSTANCE;
    }

    private ControllerManager() {
        String val = PropertyHelper.get(SPRING_SUPPORT);
        isSpringSupport = null != val && "true".equalsIgnoreCase(val.trim()) ? true : false;
    }

    private static boolean isSpringSupport;

    private static final String SPRING_SUPPORT = "spring_support";

    private static volatile ControllerManager INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(ControllerManager.class);
}
