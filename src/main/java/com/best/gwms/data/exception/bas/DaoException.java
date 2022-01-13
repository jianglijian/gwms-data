package com.best.gwms.data.exception.bas;

import java.util.ArrayList;
import java.util.List;

/**
 * @descreption:
 * @author: Created by maz on 2018/1/31.
 */
public class DaoException extends BizException {
    private List<BizClientMessage> clientMessages = new ArrayList<BizClientMessage>();

    public DaoException() {}

    public DaoException(Exception e) {
        super(e);
    }

    public DaoException(String code, String[] content) {
        /* super(code); */
        BizClientMessage message = new BizClientMessage(code, content);
        clientMessages.add(message);
    }

    public DaoException(String code) {
        /* super(code); */
        BizClientMessage message = new BizClientMessage(code, null);
        clientMessages.add(message);
    }

    @Override
    public List<BizClientMessage> getClientMessages() {
        return clientMessages;
    }

    @Override
    public void setClientMessages(List<BizClientMessage> clientMessages) {
        this.clientMessages = clientMessages;
    }
}
