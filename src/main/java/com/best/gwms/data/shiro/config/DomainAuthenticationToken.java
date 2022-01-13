package com.best.gwms.data.shiro.config;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * shiro 默认的登录token只包括用户名 和密码，这里扩展一下，添加一个domainCode字段
 * @author bl03846
 */
public class DomainAuthenticationToken extends UsernamePasswordToken {
    private static final long serialVersionUID = -4444011180986314129L;
    /**
     * 域编码
     */
    private String domainCode;

    public DomainAuthenticationToken(String username, String password, boolean rememberMe, String host, String domainCode) {
        super(username, password, rememberMe, host);
        this.domainCode = domainCode;
    }

    public String getDomainCode() {
        return domainCode;
    }

    public void setDomainCode(String domainCode) {
        this.domainCode = domainCode;
    }
}
