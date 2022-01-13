package com.best.gwms.data.shiro.filter;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Package : com.best.gwms.web.filter @Author : Shen.Ziping[zpshen@best-inc.com] @Date : 2018/1/15
 * 17:26 @Description : 跨域需要开启本filter @Version : V1.0
 */
@Configuration
public class CmnCrossFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        // log.info("************************过滤器使用CorsFilter**************");

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        //导出的时候需要先清空原缓冲区，如果放到业务那ajax调用会出现跨域的情况
        response.reset();
        response.resetBuffer();

        response.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) req).getHeader("origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, response);
        }


    }

    @Override
    public void init(FilterConfig filterConfig)  {
        //
    }

    @Override
    public void destroy()  {
        //
    }
}
