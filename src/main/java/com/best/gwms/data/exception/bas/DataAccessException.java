package com.best.gwms.data.exception.bas;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class DataAccessException extends RuntimeException implements com.best.xingng.org.apache.cxf.exception.CheckedException {

    protected static final Log logger = LogFactory.getLog(DataAccessException.class);
    private List<BizClientMessage> clientMessages = new ArrayList<BizClientMessage>();

    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public DataAccessException() {}

    public DataAccessException(Exception e) {
        super(e);
    }

    public List<BizClientMessage> getClientMessages() {
        return clientMessages;
    }

    public void setClientMessages(List<BizClientMessage> clientMessages) {
        this.clientMessages = clientMessages;
    }

    // content是一些业务上的信息 比如异常信息“单据[ORDER_NUM]状态[STA]不正确”，那么values的内容为[123456, close]
    public DataAccessException(String code, String... values) {
        super(code);
        BizClientMessage message = new BizClientMessage(code, values);
        this.clientMessages.add(message);
    }

    public DataAccessException(String code) {
        super(code);
        this.errorCode = code;
        BizClientMessage message = new BizClientMessage(code, null);
        this.clientMessages.add(message);
    }
}
