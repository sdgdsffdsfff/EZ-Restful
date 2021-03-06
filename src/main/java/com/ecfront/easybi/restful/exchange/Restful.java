package com.ecfront.easybi.restful.exchange;

import com.ecfront.easybi.restful.inner.ControllerManager;
import com.ecfront.easybi.restful.inner.RestfulExecutor;
import com.ecfront.easybi.restful.inner.security.RestfulSecurityProcessor;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * <h1>入口方法</h1>
 * <ul>*参数限制
 * <li>类型：只能为String、基本类型及其包装类、Map、带@Model的类</li>
 * <li>顺序：注解中带*的参数列表+（可选）Map+（可选）Model</li>
 * </ul>
 */
public class Restful {

    /**
     * 初始化，整个生命周期中只调用一次
     *
     * @param basePath 要扫描的根包
     */
    public void init(String basePath) throws Exception {
        ControllerManager.getInstance().scanController(basePath);
    }

    /**
     * 每次查询时调用，用于获取请求对应的信息
     *
     * @param httpMethod 请求方法类型
     * @param uri        请求URI
     * @param parameter  请求参数
     * @return 返回信息
     * @see # execute(HttpMethod httpMethod, String uri, Object model, Map<String, String[]> parameter, InputStream inputStream)
     */
    public ResponseVO excute(HttpMethod httpMethod, String uri, Map<String, String[]> parameter) throws InvocationTargetException, IllegalAccessException {
        return excute(httpMethod, uri, null, null, null, parameter);
    }

    /**
     * 每次查询时调用，用于获取请求对应的信息
     *
     * @param httpMethod 请求方法类型
     * @param uri        请求URI
     * @param model      请求包含的模型，要求此Object为@Model
     * @return 返回信息
     * @see # execute(HttpMethod httpMethod, String uri, Object model, Map<String, String[]> parameter, InputStream inputStream)
     */
    public ResponseVO excute(HttpMethod httpMethod, String uri, Object model) throws InvocationTargetException, IllegalAccessException {
        return excute(httpMethod, uri, model, null, null, null);
    }

    /**
     * 每次查询时调用，用于获取请求对应的信息
     *
     * @param httpMethod 请求方法类型
     * @param uri        请求URI
     * @param model      请求包含的模型，要求此Object为@Model
     * @param fileItems  请求文件列表
     * @param token      token
     * @param parameter  请求参数
     * @return 返回信息
     */
    public ResponseVO excute(HttpMethod httpMethod, String uri, Object model, List<FileItem> fileItems, String token, Map<String, String[]> parameter) throws InvocationTargetException, IllegalAccessException {
        if (logger.isDebugEnabled()) {
            logger.debug("Excuting... url:{},method:{}", uri, httpMethod.getCode());
        }
        ResponseVO responseVO = RestfulSecurityProcessor.getInstance().loginOrlogout(uri, parameter, token);
        if (null != responseVO) {
            return responseVO;
        }
        return RestfulExecutor.getInstance().execute(httpMethod, uri, model, parameter, fileItems, token);
    }

    public static Restful getInstance() {
        if (INSTANCE == null) {
            synchronized (Restful.class) {
                INSTANCE = new Restful();
            }
        }
        return INSTANCE;
    }

    private Restful() {
    }

    private static volatile Restful INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(Restful.class);
}
