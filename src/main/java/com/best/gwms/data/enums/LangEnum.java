/*
 * LangEnum.java
 *
 * @author bl03846 Created on 2018/8/12 22:35 版本 修改时间 修改内容 V1.0.1 2018/8/12  初始版本
 *
 * Copyright (c) 2007 百世物流科技（中国）有限公司 版权所有CHANGCHUN GENGHIS TECHNOLOGY CO.,LTD. All Rights Reserved.
 */
package com.best.gwms.data.enums;

/**    已实现语言      @author bl03846    @version 1.0.1  */
public enum LangEnum {
    /**
     * 美式英语
     */
    en_US,
    /**
     * 汉语简体
     */
    zh_CN,
    /**
     * 泰式英语
     */
    en_TH,
    /**
     * 泰语
     */
    th_TH,
    /**
     * 越南语
     */
    vi_VN;

    public static LangEnum getEnum(String str) {
        LangEnum langEnum = null;
        try {
            langEnum = LangEnum.valueOf(str);
        } catch (IllegalArgumentException e) {
            return null;
        }
        return langEnum;
    }
}
