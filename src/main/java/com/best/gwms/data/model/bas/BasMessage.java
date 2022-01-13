/*
 * BasMessage.java
 *
 * @author bl03846 Created on 2018/6/6 11:08 版本 修改时间 修改内容 V1.0.1 2018/6/6  初始版本
 *
 * Copyright (c) 2007 百世物流科技（中国）有限公司 版权所有CHANGCHUN GENGHIS TECHNOLOGY CO.,LTD. All Rights Reserved.
 */
package com.best.gwms.data.model.bas;

import java.io.Serializable;

/**    类的描述信息       @author bl03846    @version 1.0.1  */
public class BasMessage extends AbstractPo implements Serializable {
    /** 消息类型 */
    private String messageType;


    public String getMessageType() {
        return messageType;
    }


    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
