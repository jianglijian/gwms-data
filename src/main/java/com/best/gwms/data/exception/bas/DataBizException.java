package com.best.gwms.data.exception.bas;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Package : com.best.gwms.common.exception @Author : Shen.Ziping[zpshen@best-inc.com] @Date :
 * 2018/1/15 9:50 " @Version : V1.0
 */
public class DataBizException extends RuntimeException implements com.best.xingng.org.apache.cxf.exception.CheckedException {
    protected static final Log logger = LogFactory.getLog(DataBizException.class);
    private List<BizClientMessage> clientMessages = new ArrayList<BizClientMessage>();

    public DataBizException() {}

    public DataBizException(Exception e) {
        super(e);
    }

    public List<BizClientMessage> getClientMessages() {
        return clientMessages;
    }

    public void setClientMessages(List<BizClientMessage> clientMessages) {
        this.clientMessages = clientMessages;
    }

    // content是一些业务上的信息 比如异常信息“单据[ORDER_NUM]状态[STA]不正确”，那么values的内容为[123456, close]
    public DataBizException(String code, String... values) {
        super(code);
        BizClientMessage message = new BizClientMessage(code, values);
        this.clientMessages.add(message);
    }

    public DataBizException(String code) {
        super(code);
        BizClientMessage message = new BizClientMessage(code, null);
        this.clientMessages.add(message);
    }
}
