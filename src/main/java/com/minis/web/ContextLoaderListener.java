package com.minis.web;

import com.minis.beans.BeansException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {
    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
    private WebApplicationContext context; // Ioc容器

    public ContextLoaderListener() {
    }
    public ContextLoaderListener(WebApplicationContext context) {
        this.context = context;
    }
    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            initWebApplicationContext(event.getServletContext());
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }
    private void initWebApplicationContext(ServletContext servletContext) throws BeansException {
        String sContextLocation = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
        WebApplicationContext wac = new XmlWebApplicationContext(sContextLocation);
        wac.setServletContext(servletContext);
        this.context = wac;
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
    }
}