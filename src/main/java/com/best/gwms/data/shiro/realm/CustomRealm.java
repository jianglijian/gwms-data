package com.best.gwms.data.shiro.realm;

import com.best.gwms.common.base.ClientInfo;
import com.best.gwms.common.exception.BizException;
import com.best.gwms.common.exception.VerificationCodeException;
import com.best.gwms.common.exception.domain.DomainExceptionCode;
import com.best.gwms.common.vo.bas.BasCodeInfoVo;
import com.best.gwms.common.vo.bas.BasDomainVo;
import com.best.gwms.common.vo.bas.BasUserVo;
import com.best.gwms.data.constant.FieldConstant;
import com.best.gwms.data.shiro.config.CustomRealmAdapterService;
import com.best.gwms.data.util.EasyUtil;
import com.best.gwms.master.xing.BasCodeXingService;
import com.best.gwms.master.xing.BasDomainXingService;
import com.best.gwms.master.xing.BasUserXingService;
import com.google.code.kaptcha.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.best.gwms.data.util.EasyUtil.isStringNotEmpty;
import static com.best.gwms.util.EasyUtil.isStringEmpty;
import static com.best.gwms.util.EasyUtil.isStringNotEmpty;

/**
 * @author bl03846
 */
@Slf4j
public class CustomRealm extends AuthorizingRealm implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    // 身份授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 功能权限鉴权
        return new SimpleAuthorizationInfo();
    }

    @Override
    // 身份验证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest httpServletRequest = sra.getRequest();
        String validateCode = (String) SecurityUtils.getSubject().getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        String randomCode = httpServletRequest.getParameter(FieldConstant.RANDOM_CODE);

        // 手持端登录会传deviceType，PC端登录不会
        String deviceType = httpServletRequest.getParameter("deviceType");
        if (EasyUtil.isStringEmpty(deviceType) && validateCode != null && !validateCode.equalsIgnoreCase(randomCode)) {
            // 图片验证码错误
            throw new VerificationCodeException();
        }

        BasUserXingService userXingService = applicationContext.getBean(CustomRealmAdapterService.class).getUserXingService();
        BasCodeXingService codeXingService = applicationContext.getBean(CustomRealmAdapterService.class).getCodeXingService();
        BasDomainXingService domainXingService = applicationContext.getBean(CustomRealmAdapterService.class).getDomainXingService();

        String userCode = ((UsernamePasswordToken) authenticationToken).getUsername();
        char[] password = ((UsernamePasswordToken) authenticationToken).getPassword();

        // 根据Code查询User
        BasUserVo user = userXingService.getByUserCodeForShiro(userCode);

        BasCodeInfoVo userStatusInactiveCode = codeXingService.getBasStatusInactiveCode();
        BasCodeInfoVo userLockStateCode = codeXingService.getBasLockCode();

        if (user == null) {
            // 用户不存在
            throw new UnknownAccountException();
        }

        if (EasyUtil.isObjEqual(user.getStatus(), userStatusInactiveCode.getId())) {
            // 帐号已停用
            throw new DisabledAccountException();
        } else if (EasyUtil.isObjEqual(user.getLockState(), userLockStateCode.getId())) {
            // 帐号已被锁定
            throw new LockedAccountException();
        } else if (password == null || password.length == 0) {
            // 密码是否为空
            throw new IncorrectCredentialsException();
        }

        String lang = httpServletRequest.getParameter("lang");
        if (EasyUtil.isStringNotEmpty(lang)) {
            user.setLanguage(lang);
        }

        String domainCode = user.getDomainName();

        // 用户的 domainCode 不能为空
        if (isStringEmpty(domainCode)) {
            throw new BizException(DomainExceptionCode.ERR_BAS_DOMAINCODE_EMPTY);
        }

        // 校验domain
        BasDomainVo domainVo = domainXingService.getDomainByCodeWithOutPackage(new ClientInfo(), domainCode);

        if (domainVo == null) {
            throw new BizException(DomainExceptionCode.ERR_DOMAIN_INVALID, domainCode);
        }

        // 若域已为inactive状态，则直接返回帐号被停用
        if (userStatusInactiveCode.getId().equals(domainVo.getStatus())) {
            // 域已被停用
            throw new BizException(DomainExceptionCode.ERR_DOMAIN_INVALID,domainVo.getCode());
        }

        // 密码校验
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principalCollection) {
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principalCollection, getName());
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
