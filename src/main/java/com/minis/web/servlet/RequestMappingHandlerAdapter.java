package com.minis.web.servlet;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minis.beans.BeansException;
import com.minis.beans.factory.annotation.Autowired;
import com.minis.web.*;
import com.minis.web.annotation.ResponseBody;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class RequestMappingHandlerAdapter implements HandlerAdapter {
    WebApplicationContext wac;

    @Autowired
    private WebBindingInitializer webBindingInitializer; // 数据传入时候
    @Autowired
    private HttpMessageConverter messageConverter = null; // 数据传出时

//    public RequestMappingHandlerAdapter(WebApplicationContext wac) throws BeansException {
//        this.wac = wac;
//    }

    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        handleInternal(request, response, (HandlerMethod) handler);
    }
    private void handleInternal(HttpServletRequest request, HttpServletResponse response,
                                HandlerMethod handler) {
        try {
            invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 反射调用方法并且绑定数据
    protected void invokeHandlerMethod(HttpServletRequest request,
                                       HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();
        Parameter[] methodParameters = handlerMethod.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParameters.length];
        int i = 0;
        //对调用方法里的每一个参数，处理绑定
        for (Parameter methodParameter : methodParameters) {
            Class<?> type = methodParameter.getType();
            Object methodParamObj = type.newInstance(); // 基本数据类型异常
            // 给这个参数创建WebDataBinder   methodParamObj: user
            WebDataBinder wdb = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());

            //注册binder中的editor-- 自定义editer
            webBindingInitializer.initBinder(wdb);

            wdb.bind(request);

            methodParamObjs[i++] = methodParamObj;
        }

        // 调用指定方法
        Method invocableMethod = handlerMethod.getMethod();
        Object returnObj = invocableMethod.invoke(handlerMethod.getBean(), methodParamObjs);

        // 对返回的数据进行格式转换
        if (invocableMethod.isAnnotationPresent(ResponseBody.class)){
            this.messageConverter.write(returnObj, response);
        }

        response.getWriter().append(returnObj.toString());
    }

}