/*
 * JobAnnotation.java
 *
 * @author bl03846 Created on 2018/3/2 15:08 版本 修改时间 修改内容 V1.0.1 2018/3/2  初始版本
 *
 * Copyright (c) 2007 百世物流科技（中国）有限公司 版权所有CHANGCHUN GENGHIS TECHNOLOGY CO.,LTD. All Rights Reserved.
 */
package com.best.gwms.data.annotation;

import java.lang.annotation.*;

/**    
 * 标记字段是日期,目前用于数据导出       
 * @author bl03846    
 * @version 1.0.1 
 * */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DateExport {

    String methodName() default "";
}
