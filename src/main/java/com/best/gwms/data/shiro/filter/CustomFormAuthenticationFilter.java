package com.best.gwms.data.shiro.filter;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.best.gwms.data.constant.FieldConstant;
import com.best.gwms.data.shiro.config.DomainAuthenticationToken;
import com.google.code.kaptcha.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author bl03846
 */
@Component
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {
    private static final Log log = LogFactory.get();

    private String serverLocalhost = FieldConstant.LOCALHOST;

    /**
     * 添加域code
     */
    private String domainCodeParam = FieldConstant.DOMAIN_CODE;

    public String getDomainCode() {
        return domainCodeParam;
    }

    protected String getDomainCode(ServletRequest request) {
        return WebUtils.getCleanParam(request, getDomainCode());
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        String domainCode = getDomainCode(request);
        boolean rememberMe = isRememberMe(request);
        String host = getHost(request);
        return new DomainAuthenticationToken(username, password, rememberMe, host, domainCode);
    }

    /**
     * 默认的登录用户名是:username,我们用的是user，所以这里要改写一下
     * @return
     */
    @Override
    public String getUsernameParam() {
        return FieldConstant.LOGIN_CODE_FIELD;
    }

    @Bean
    public FilterRegistrationBean registration(CustomFormAuthenticationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    /** 原FormAuthenticationFilter的认证方法 如果校验失败，将验证码错误失败信息，通过shiroLoginFailure设置到request中 */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();
        String validateCode = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String randomCode = httpServletRequest.getParameter(FieldConstant.RANDOM_CODE);
        if (randomCode != null && validateCode != null && !randomCode.equalsIgnoreCase(validateCode)) {
            httpServletRequest.setAttribute(FieldConstant.SHIRO_LOGIN_FAILURE, FieldConstant.RANDOM_CODE_ERROR);
            return true;
        }

        HttpServletRequest request1 = (HttpServletRequest) request;

        // 总是有莫名的http://127.0.0.1/的访问
        if (request1.getRequestURL().indexOf(serverLocalhost) > -1) {

            return super.onAccessDenied(request, response);
        }
        log.info("default login url is:{}", getLoginUrl());
        log.info("login url from request is:{}", WebUtils.getPathWithinApplication(WebUtils.toHttp(request)));
        if (isLoginRequest(request, response)) {
            log.info("isLoginRequest: true");
            if (isLoginSubmission(request, response)) {
                log.info("isLoginSubmission: false");
                return executeLogin(request, response);
            } else {
                log.info("isLoginSubmission: true");
                return true;
            }
        } else {
            log.info("isLoginRequest: false");
            HttpServletResponse res = WebUtils.toHttp(response);
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    /** 登录成功后自动调用,打上成功标记，无论成功失败都执行controller */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) {

        request.setAttribute(FieldConstant.SHIRO_LOGIN_SUCCESS, FieldConstant.YES);

        return true;
    }

    /**
     * 登陆退出，换账号登陆处理，使每次登陆都生成新的token
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest request1 = (HttpServletRequest) request;
        if (SecurityUtils.getSubject().isAuthenticated() && WebUtils.getPathWithinApplication(request1).equals(FieldConstant.LOGIN_URL)) {
            return false;
        }

        return super.isAccessAllowed(request, response, mappedValue);
    }
}
