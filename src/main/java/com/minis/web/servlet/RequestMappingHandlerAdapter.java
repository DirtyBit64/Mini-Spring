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

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler){
        return handleInternal(request, response, (HandlerMethod) handler);
    }
    private ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response,
                                HandlerMethod handler) {
        ModelAndView mv = null;
        try {
            mv = invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    // 反射调用方法并且绑定数据
    protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
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

        ModelAndView mav = null;
        // 如果是ResponseBody注解，仅仅返回值，则转换数据格式后直接写到response
        if (invocableMethod.isAnnotationPresent(ResponseBody.class)){ //ResponseBody
            this.messageConverter.write(returnObj, response);
        } else { //返回的是前端页面
            if (returnObj instanceof ModelAndView) {
                mav = (ModelAndView)returnObj;
            }
            else if(returnObj instanceof String) { //字符串也认为是前端页面
                String sTarget = (String)returnObj;
                mav = new ModelAndView();
                mav.setViewName(sTarget);
            }
        }
        if(returnObj != null) {
            response.getWriter().append(returnObj.toString());
        }

        return mav;
    }

}