package com.minis.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {
    private Map<String, MappingValue> mappingValues;
    private Map<String, Class<?>> mappingClz = new HashMap<>();
    private Map<String, Object> mappingObjs = new HashMap<>();

    private String sContextConfigLocation;

    // 首次访问（创建）servlet时调用一次
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // 从web.xml中拿到初始化参数（这里是mvc的配置文件url字符串）
        sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(sContextConfigLocation);
        } catch (MalformedURLException e) {
            // log.error("dispatcherServlet解析url异常", e);
            System.out.println("dispatcherServlet解析url异常" + e);
        }
        Resource rs = new ClassPathXmlResource(xmlPath);
        XmlConfigReader reader = new XmlConfigReader();
        mappingValues = reader.loadConfig(rs);
        Refresh();
    }

    //对所有的mappingValues中注册的类进行实例化，默认构造函数
    protected void Refresh() {
        for (Map.Entry<String,MappingValue> entry : mappingValues.entrySet()) {
            String id = entry.getKey();
            String className = entry.getValue().getClz();
            Object obj = null;
            Class<?> clz = null;
            try {
                clz = Class.forName(className);
                obj = clz.newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                // log.error("dispatcherServlet中refresh()里类加载异常", e);
                System.out.println("dispatcherServlet中refresh()里类加载异常" + e);
                e.printStackTrace();
            }
            mappingClz.put(id, clz);
            mappingObjs.put(id, obj);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sPath = request.getServletPath(); //获取请求的path
        if (this.mappingValues.get(sPath) == null) {
            return;
        }

        Class<?> clz = this.mappingClz.get(sPath); //获取bean类定义
        Object obj = this.mappingObjs.get(sPath);  //获取bean实例
        String methodName = this.mappingValues.get(sPath).getMethod(); //获取调用方法名
        Object objResult = null;
        try {
            Method method = clz.getMethod(methodName);
            objResult = method.invoke(obj); //方法调用
        } catch (Exception e) {
            // log.error("dispatcherServlet中doGet()里反射调用方法异常", e);
            System.out.println("dispatcherServlet中doGet()里反射调用方法异常" + e);
        }
        //将方法返回值写入response
        response.getWriter().append(objResult.toString());
    }

}

