package com.best.gwms.data.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("开始请求地址拦截");
        HttpSession session = request.getSession(false);
        String path = request.getRequestURI();

        if(path.contains("/trackInfo/feedback/store")){
            return true;
        }

        if (session != null && session.getAttribute("user") != null) {
            return true;
        } else  {
            logger.info("access deny====================================");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        logger.info("返回视图或String之前的处理");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        logger.info("返回视图或String之前的处理");
    }
}
