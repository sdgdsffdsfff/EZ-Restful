package com.ecfront.easybi.restful.exchange;

import com.alibaba.fastjson.JSON;
import com.ecfront.easybi.restful.inner.ConfigContainer;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.List;

/**
 * <h1>Servlet 代理类</1h>
 * <p>在Web.xml中配置此类:</p>
 * <servlet>
 * <servlet-name>servlet</servlet-name>
 * <servlet-class>com.ecfront.easybi.restful.exchange.RestfulServlet</servlet-class>
 * </servlet>
 * <servlet-mapping>
 * <servlet-name>servlet</servlet-name>
 * <url-pattern>/api/*</url-pattern><!-- 视情况而定-->
 * </servlet-mapping>
 */
public class RestfulServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        if (logger.isDebugEnabled()) {
            logger.debug("Init,Load Scan Base Path:{}", ConfigContainer.SCAN_BASE_PATH);
        }
        try {
            Restful.getInstance().init(ConfigContainer.SCAN_BASE_PATH);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("RestfulServlet error:{}", e);
            }
        }
        factory.setRepository((File) this.getServletConfig().getServletContext().getAttribute("javax.servlet.context.tempdir"));
        factory.setSizeThreshold(1024 * 100);
        servletFileUpload = new ServletFileUpload(factory);
    }

    private void process(HttpServletRequest request, HttpServletResponse response,
                         HttpMethod methodType) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String pathInfo = URLDecoder.decode(request.getPathInfo(), "UTF-8");
        pathInfo = pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("pragma", "no-cache");
        response.setHeader("cache-control", "no-cache");
        if (logger.isDebugEnabled()) {
            logger.debug("Processing... url:{},method:{}", pathInfo, methodType.getCode());
        }
        ResponseVO vo;
        List<FileItem> fileItems = null;
        Object model = null;
        try {
            if (null != request.getContentType() && -1 != request.getContentType().toLowerCase().indexOf("multipart/form-data")) {
                fileItems = servletFileUpload.parseRequest(request);
            } else {
                StringWriter writer = new StringWriter();
                IOUtils.copy(request.getInputStream(), writer, "UTF-8");
                String content = writer.toString();
                model = null != content && !"".equals(content.trim()) ? JSON.parse(URLDecoder.decode(content, "UTF-8")) : null;
            }
            vo = Restful.getInstance().excute(methodType, pathInfo, model, fileItems, request.getParameter(ConfigContainer.TOKEN), request.getParameterMap());
            if (logger.isDebugEnabled()) {
                logger.debug("Processed... url:{},method:{}", pathInfo, methodType.getCode());
            }
        } catch (Exception e) {
            vo = RestfulHelper.unknownError();
            if (logger.isErrorEnabled()) {
                logger.error("Process error:{}", e);
            }
        }
        if (request.getParameterMap().containsKey(ConfigContainer.JSONP_CALLBACK)) {
            response.getWriter().print(ConfigContainer.JSONP_CALLBACK + "(" + JSON.toJSONString(vo) + ")");
        } else {
            response.getWriter().print(JSON.toJSONString(vo));
        }
    }


    private HttpMethod getMethodType(HttpServletRequest req) {
        String type = req.getParameter(ConfigContainer.METHOD_TYPE);
        if (null != type && !"".equalsIgnoreCase(type.trim())) {
            if ("PUT".equalsIgnoreCase(type)) {
                return HttpMethod.PUT;
            } else if ("POST".equalsIgnoreCase(type)) {
                return HttpMethod.POST;
            } else if ("DELETE".equalsIgnoreCase(type)) {
                return HttpMethod.DELETE;
            } else if ("GET".equalsIgnoreCase(type)) {
                return HttpMethod.GET;
            }
        }
        return null;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpMethod type = getMethodType(req);
        process(req, resp, null != type ? type : HttpMethod.DELETE);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpMethod type = getMethodType(req);
        process(req, resp, null != type ? type : HttpMethod.GET);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpMethod type = getMethodType(req);
        process(req, resp, null != type ? type : HttpMethod.POST);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpMethod type = getMethodType(req);
        process(req, resp, null != type ? type : HttpMethod.PUT);
    }

    private static final DiskFileItemFactory factory = new DiskFileItemFactory();
    private static ServletFileUpload servletFileUpload;

    private static final Logger logger = LoggerFactory.getLogger(RestfulServlet.class);
}
