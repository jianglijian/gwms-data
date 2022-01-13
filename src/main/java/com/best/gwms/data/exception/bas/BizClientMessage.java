package com.best.gwms.data.exception.bas;

import com.best.gwms.data.util.EasyUtil;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Package : com.best.gwms.common.exception @Author : Shen.Ziping[zpshen@best-inc.com] @Date :
 * 2018/1/17 11:36 " @Version : V1.0
 */
public class BizClientMessage {
    private String code;
    private String[] content;

    public BizClientMessage() {}

    public BizClientMessage(String code, String... content) {
        setCode(code);
        setContent(content);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        if (content == null) {
            return;
        }
        List<String> contents = Lists.newArrayList(content);
        this.content = contents.stream().filter(EasyUtil::isStringNotEmpty).toArray(String[]::new);

    }
}
