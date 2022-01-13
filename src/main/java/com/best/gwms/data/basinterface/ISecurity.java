
package com.best.gwms.data.basinterface;
import com.best.gwms.common.base.ClientInfo;
import com.best.gwms.data.constant.DateFormatConstant;
import com.best.gwms.data.constant.FieldConstant;
import com.best.gwms.data.model.bas.LoginUser;
import com.best.gwms.data.util.DataStatic;
import com.best.gwms.data.util.EasyUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 *    类的描述信息       @author bl03846    @version 1.0.1 
 */
public interface ISecurity extends ITimeZone, ILocalism {

    Logger log = LoggerFactory.getLogger(ISecurity.class);

    /**
     * 获取当前用户的Session
     *
     * @return
     */
    default Session getCurrentSession() {
        return new SimpleSession();
        //return SecurityUtils.getSubject().getSession();
    }

    // 登陆成功后会把userSession放到Session中( Session.setAttribut(sessionId, userSession) )
    default HttpSession getCurrentUserSession() {
        HttpSession userSession = DataStatic.getClientInfoHL();
        if (userSession != null) {
            return userSession;
        }
        userSession = (HttpSession) getSessionAttribute(getCurrentSession().getId());
        DataStatic.addSession(userSession);
        return userSession;
    }

    /**
     * 获取当前用户ID
     *
     * @return
     */
    default Long getCurrentUserId() {
        if (getCurrentUserSession() == null) {
            return null;
        }
        HttpSession session = getCurrentUserSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("user");
        return loginUser.getLoginUser().getId();

    }


    /**
     * 获取当前用户ID
     *
     * @return
     */
    default String getCurrentUserAccount() {

        return getLoginUser().getLoginUser().getLoginCode();

    }

    default LoginUser getLoginUser(){
        if (getCurrentUserSession() == null) {
            return null;
        }
        HttpSession session = getCurrentUserSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("user");

        return loginUser;

    }




    // 调用master的xingng服务时要传clientinfo
    default ClientInfo getClientInfo() {

        HttpSession userSession = getCurrentUserSession();
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setTimeZone(FieldConstant.DEFAULT_TIMEZONE);
        if (null == userSession) {
            log.info("User Session is null.");
            return clientInfo;
        }

        HttpSession session = getCurrentUserSession();
        LoginUser loginUser = (LoginUser) session.getAttribute("user");
        clientInfo = new ClientInfo(loginUser.getLoginUser().getDomainId(), loginUser.getLoginUser().getDefaultWhId(), null,loginUser.getLoginUser().getLanguage());
        clientInfo.setUserId(getCurrentUserId());
        return clientInfo;
    }

    /**
     * 把值放入到当前登录用户的Session里
     *
     * @param key
     * @param value
     */
    default void setSessionAttribute(Object key, Object value) {
        getCurrentSession().setAttribute(key, value);
    }

    /**
     * 从当前登录用户的Session里取值
     *
     * @param key
     * @return
     */
    default Object getSessionAttribute(Object key) {
        return getCurrentSession().getAttribute(key);
    }

    default String getKaptcha(String key) {
        String kaptcha = (String) getSessionAttribute(key);
        getCurrentSession().removeAttribute(key);
        return kaptcha;
    }

    /**
     * 获取验证码，获取一次后删除
     *
     * @return
     */
    default String getYZM() {
        String code = (String) getCurrentSession().getAttribute("CODE");
        getCurrentSession().removeAttribute("CODE");
        return code;
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    default boolean isLogin() {
        return null != SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 获取语言
     */
    default String getLang() {


        return getCurrentUserSession() != null ? getLoginUser().getLoginUser().getLanguage() : null;
    }

    /**
     * 获取时区
     */
    @Override
    default String getTimeZone() {
        return (getCurrentUserSession() != null && EasyUtil.isStringNotEmpty(getLoginUser().getLoginUser().getWarehouseZone())) ? getLoginUser().getLoginUser().getWarehouseZone() : FieldConstant.DEFAULT_TIMEZONE;
    }

    /**
     * 获取当前登录用户对应的日期方言
     *
     * @return
     */
    @Override
    default Locale getLocale() {
        return DateFormatConstant.getLocal(getLang());
    }


}
