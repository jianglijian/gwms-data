package com.best.gwms.data.exception.bas;


/**
 * 登录异常类
 */
public class LoginException extends DataBizException {
    public LoginException() {
    }

    public LoginException(String code, String[] content) {
        BizClientMessage message = new BizClientMessage(code, content);
        super.getClientMessages().add(message);
    }

    public LoginException(String code) {
        BizClientMessage message = new BizClientMessage(code, null);
        super.getClientMessages().add(message);
    }
}
