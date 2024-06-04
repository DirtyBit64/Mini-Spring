package com.minis.web.servlet;

import com.minis.beans.BeansException;
import com.minis.web.AnnotationConfigWebApplicationContext;
import com.minis.web.RequestMapping;
import com.minis.web.WebApplicationContext;
import com.minis.web.XmlScanComponentHelper;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {
    private List<String> packageNames;// 用于存储需要扫描的package列表

    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;

    private Map<String,Object> controllerObjs = new HashMap<>();// 用于存储controller的名称与对象的映射关系
    private List<String> controllerNames;// 用于存储controller名称数组列表
    private Map<String,Class<?>> controllerClasses = new HashMap<>();// 用于存储controller名称与类的映射关系

    private String sContextConfigLocation;

    private WebApplicationContext webApplicationContext;
    private WebApplicationContext parentApplicationContext;

    // 首次访问（创建）servlet时调用一次
    @Override
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
        // 父上下文来自创建于listener初始化
        this.parentApplicationContext = (WebApplicationContext) this.getServletContext()
                .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        // 从web.xml中拿到初始化参数（这里是mvc的配置文件url字符串）
        sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(sContextConfigLocation);
        } catch (MalformedURLException e) {
            // log.error("dispatcherServlet解析url异常", e);
            System.out.println("dispatcherServlet解析url异常" + e);
        }
        this.packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        // 再启动一个上下文，专门处理request
        this.webApplicationContext = new AnnotationConfigWebApplicationContext(sContextConfigLocation, this.parentApplicationContext);
        try {
            Refresh();
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }

    //对所有的mappingValues中注册的类进行实例化，默认构造函数
    protected void Refresh() throws BeansException {
        // 1.初始化controller
        initController();
        // 2.初始化URL映射
        initHandlerMappings(this.webApplicationContext);
        initHandlerAdapters(this.webApplicationContext);
    }

    protected void initHandlerMappings(WebApplicationContext wac) {
        this.handlerMapping = new RequestMappingHandlerMapping(wac);
    }
    protected void initHandlerAdapters(WebApplicationContext wac) throws BeansException {
        this.handlerAdapter = new RequestMappingHandlerAdapter(wac);
    }

    // 这实例化controller bean
    protected void initController() {
        //扫描包，获取所有类名
        this.controllerNames = scanPackages(this.packageNames);
        for (String controllerName : this.controllerNames) {
            Object obj;
            Class<?> clz = null;
            try {
                clz = Class.forName(controllerName); //加载类
                this.controllerClasses.put(controllerName, clz);
            } catch (Exception e) {
                System.out.println("initController()加载类异常:" + e);
            }
            try {
                obj = clz.newInstance(); //实例化bean
                this.controllerObjs.put(controllerName, obj);
            } catch (Exception e) {
                System.out.println("initController()实例化bean异常:" + e);
            }
        }
    }

    private List<String> scanPackages(List<String> packages) {
        List<String> tempControllerNames = new ArrayList<>();
        for (String packageName : packages) {
            tempControllerNames.addAll(scanPackage(packageName));
        }
        return tempControllerNames;
    }
    private List<String> scanPackage(String packageName) {
        List<String> tempControllerNames = new ArrayList<>();
        URI uri;
        File dir = null;
        // 将以.分隔的包名换成以/分隔的uri
        try {
            // file:/D:/JAVA/Mini-Spring/out/artifacts/mini_mvc/WEB-INF/classes/com/minis/testBean/
            uri = this.getClass().getResource("/" + packageName.replaceAll("\\.", "/")).toURI();
            dir = new File(uri);
        } catch (Exception e) {
            System.out.println("扫描包异常," + e);
        }
        //处理对应的文件目录
        for (File file : dir.listFiles()) { //目录下的文件或者子目录
            if(file.isDirectory()){ //对子目录递归扫描
                scanPackage(packageName+"."+file.getName());
            }else{ //类文件
                String controllerName = packageName +"." +file.getName().replace(".class", "");
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);
        try {
            doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HandlerMethod handlerMethod = this.handlerMapping.getHandler(request);
        if (handlerMethod != null) {
            HandlerAdapter ha = this.handlerAdapter;
            ha.handle(request, response, handlerMethod);
        }
    }

}

