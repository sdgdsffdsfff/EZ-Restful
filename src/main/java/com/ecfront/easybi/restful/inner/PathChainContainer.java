package com.ecfront.easybi.restful.inner;

import com.ecfront.easybi.restful.exchange.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>URI路径链容器</h1>
 * <p>此为核心类，实现URI到业务代码的映射逻辑 </p>
 * <h2>示例说明</h2>
 * <p>某类@Uri("group\/")，其中的一个方法为@Get("*\/user\/*")</p>
 * <p>对应的访问URI可以为:group/1/user/2/,其中1表示group的ID,2表示user的ID</p>
 * <p>其存储方式如下</p>
 * <p>GET</p>
 * <p>----group</p>
 * <p>------*</p>
 * <p>--------user</p>
 * <p>----------*</p>
 */
public class PathChainContainer {

    /**
     * 解析请求
     *
     * @param httpMethod 请求方法
     * @param uri        请求URI
     * @return 找到（反射对象）信息
     */
    public Object[] parsePath(HttpMethod httpMethod, String uri) {
        if (logger.isDebugEnabled()) {
            logger.debug("Parsing Path,method:{},uri:{}", httpMethod.getCode(), uri);
        }
        Map<String, PathNode> currentPathChain = PATH_CHAIN.get(httpMethod);
        PathNode currentPathNode;
        String[] paths = uri.split("/");
        String path;
        int length = paths.length;
        List<String> args = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            path = paths[i].trim();
            //先查找有明确对应的节点 ，如不存在再查询当前有没有带*的节点
            if (currentPathChain.containsKey(path)) {
                currentPathNode = currentPathChain.get(path);
            } else if (currentPathChain.containsKey("*")) {
                currentPathNode = currentPathChain.get("*");
                //带*节点要把对应的值加入到参数列表中，比如 @Get("/user/*/")，请求：user/111/，111要加入到参数列表
                args.add(path);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Not find,method:{},uri:{}", httpMethod.getCode(), uri);
                }
                return null;
            }
            if (i + 1 == length) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Parsed Path,reflectMethod:{},args size:{}", currentPathNode.reflectMethod.getName(), args.size());
                }
                return new Object[]{currentPathNode.reflectObject, currentPathNode.reflectMethod, args};
            }
            currentPathChain = currentPathNode.children;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Not find,method:{},uri:{}", httpMethod.getCode(), uri);
        }
        return null;
    }

    /**
     * 添加路径
     *
     * @param httpMethod 请求方法
     * @param uri        请求URI
     * @param object     反射获取的对象
     * @param method     反射获取的方法
     */
    public void addPaths(HttpMethod httpMethod, String uri, Object object, Method method) {
        switch (httpMethod) {
            case GET:
                addPaths(PATH_CHAIN.get(HttpMethod.GET), uri, object, method);
                break;
            case POST:
                addPaths(PATH_CHAIN.get(HttpMethod.POST), uri, object, method);
                break;
            case PUT:
                addPaths(PATH_CHAIN.get(HttpMethod.PUT), uri, object, method);
                break;
            case DELETE:
                addPaths(PATH_CHAIN.get(HttpMethod.DELETE), uri, object, method);
                break;
        }
    }

    /**
     * 添加路径
     *
     * @param pathChain 请求方法对应的路径链（第二层级）
     * @param uri       请求URI
     * @param object    反射获取的对象
     * @param method    反射获取的方法
     */
    private void addPaths(Map<String, PathNode> pathChain, String uri, Object object, Method method) {
        String[] paths = uri.split("/");
        Map<String, PathNode> currentPathChain = pathChain;
        PathNode currentPathNode;
        String path;
        int length = paths.length;
        //循环添加
        for (int i = 0; i < length; i++) {
            path = paths[i].trim();
            if ("".equals(path)) {
                break;
            }
            if (currentPathChain.containsKey(path)) {
                //当前节点已存在
                currentPathNode = currentPathChain.get(path);
            } else {
                //当前节点不存在，新建
                currentPathNode = new PathNode();
                currentPathChain.put(path, currentPathNode);
            }
            if (i + 1 == length) {
                //仅在最后一层时添加
                currentPathNode.reflectObject = object;
                currentPathNode.reflectMethod = method;
            }
            currentPathChain = currentPathNode.children;
        }
    }

    private static Map<HttpMethod, Map<String, PathNode>> PATH_CHAIN;

    private PathChainContainer() {
        PATH_CHAIN = new HashMap<>();
        PATH_CHAIN.put(HttpMethod.GET, new HashMap<String, PathNode>());
        PATH_CHAIN.put(HttpMethod.POST, new HashMap<String, PathNode>());
        PATH_CHAIN.put(HttpMethod.PUT, new HashMap<String, PathNode>());
        PATH_CHAIN.put(HttpMethod.DELETE, new HashMap<String, PathNode>());
    }

    public static PathChainContainer getInstance() {
        if (INSTANCE == null) {
            synchronized (PathChainContainer.class) {
                INSTANCE = new PathChainContainer();
            }
        }
        return INSTANCE;
    }

    private static volatile PathChainContainer INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(PathChainContainer.class);

    /**
     * 路径链对象
     */
    private class PathNode {
        //当前节点反射获取的对象
        public Object reflectObject;
        //当前节点反射获取的方法
        public Method reflectMethod;
        //当前节点的子节点
        public Map<String, PathNode> children;

        private PathNode() {
            this.children = new HashMap<>();
        }

    }
}
