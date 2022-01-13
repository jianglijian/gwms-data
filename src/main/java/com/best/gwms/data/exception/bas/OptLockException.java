package com.best.gwms.data.exception.bas;

/**
 * @descreption:
 * @author: Created by maz on 2018/1/31.
 */
public class OptLockException extends BizException {
    // private List<BizClientMessage> clientMessages = new ArrayList<BizClientMessage>();

    public OptLockException() {}

    public OptLockException(String code, String[] content) {
        /* super(code); */
        BizClientMessage message = new BizClientMessage(code, content);
        super.getClientMessages().add(message);
    }

    public OptLockException(String code) {
        /* super(code); */
        BizClientMessage message = new BizClientMessage(code, null);
        super.getClientMessages().add(message);
    }
}
