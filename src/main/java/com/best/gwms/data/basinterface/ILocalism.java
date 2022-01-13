/*
 * ILocalism.java
 *
 * @author bl03846 Created on 2018/8/22 15:19 版本 修改时间 修改内容 V1.0.1 2018/8/22  初始版本
 *
 * Copyright (c) 2007 百世物流科技（中国）有限公司 版权所有CHANGCHUN GENGHIS TECHNOLOGY CO.,LTD. All Rights Reserved.
 */
package com.best.gwms.data.basinterface;

import java.util.Locale;

/** 
   获取日期方言
 *  
  @author bl03846  
 * @version 1.0.1 
 */
public interface ILocalism {

    /**
     * 获取当前登录用户对应的日期方言
     * @return
     */
    Locale getLocale();

}
