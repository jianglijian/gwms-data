package com.best.gwms.data.util;


import com.best.gwms.data.constant.FieldConstant;
import com.best.gwms.data.model.bas.LoginUser;

import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;

/**
 * @descreption:
 * @author: Created by maz on 2018/2/5.
 */
public class DataStatic {
    private static ThreadLocal<HttpSession> clientInfoHL = new ThreadLocal<HttpSession>();

    static {
        // 放在一个统一的的方法，便于事务结束后统一销毁（线程池缓存的问题）
        ThreadLocalUtil.addThreadLocal2List(clientInfoHL);
    }

    private DataStatic() {
    }

    public static void addSession(HttpSession userSession) {

        clientInfoHL.set(userSession);
    }

    public static HttpSession getClientInfoHL() {
        return clientInfoHL.get();
    }

    public static void setClientInfoTH(HttpSession clientInfo) {
        clientInfoHL.set(clientInfo);
    }

    public static Long getUserId() {
        Long userId = null;
        if (getClientInfoHL() != null) {
            LoginUser user = (LoginUser) getClientInfoHL().getAttribute("user");
            userId = user.getLoginUser().getId();
        }
        return userId;
    }


    public static String getTimeZone() {

        String timeZone = "PRC";
        if (getClientInfoHL() != null) {
            LoginUser user = (LoginUser) getClientInfoHL().getAttribute("user");
            timeZone = user.getLoginUser().getWarehouseZone();
        }
        return timeZone;
    }

    public static String getLang() {
        String lang = "en_TH";
        if (getClientInfoHL() != null) {
            LoginUser user = (LoginUser) getClientInfoHL().getAttribute("user");
            lang = user.getLoginUser().getLanguage();
        }
        return lang;


    }

    public static DateTimeFormatter getWholeDateFormatter() {
        String lang = getLang() == null ? FieldConstant.DEFAULT_INNER_LANG : getLang();
        return DateTimeFormatter.ofPattern(DateGen.COMMON_DATE_FORMAT);
    }


}
