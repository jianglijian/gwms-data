/*
 * CustomRolesAuthorizationFilter.java
 *
 * @author bl03846 Created on 2018/2/5 14:33 版本 修改时间 修改内容 V1.0.1 2018/2/5  初始版本
 *
 * Copyright (c) 2007 百世物流科技（中国）有限公司 版权所有CHANGCHUN GENGHIS TECHNOLOGY CO.,LTD. All Rights Reserved.
 */
package com.best.gwms.data.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**    重写角色认证的filter      @author bl03846    @version 1.0.1  */
@Component
public class CustomRolesAuthorizationFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest req, ServletResponse resp, Object mappedValue) {
        Subject subject = getSubject(req, resp);
        String[] rolesArray = (String[]) mappedValue;
        // 没有角色限制，有权限访问
        if (rolesArray == null || rolesArray.length == 0) {
            return true;
        }
        for (int i = 0; i < rolesArray.length; i++) {
            // 若当前用户是rolesArray中的任何一个，则有权限访问
            if (subject.hasRole(rolesArray[i])) {
                return true;
            }
        }

        return false;
    }
}
