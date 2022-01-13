package com.best.gwms.data.model.bas;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Package : com.best.gwms.common.base @Author : Shen.Ziping[zpshen@best-inc.com] @Date : 2018/2/1
 * 14:54 " @Version : V1.0
 */
public class CodeMessage {
    @ApiModelProperty("异常code")
    private String code;

    @ApiModelProperty("异常描述")
    private String message;

    public CodeMessage() {}

    public CodeMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
