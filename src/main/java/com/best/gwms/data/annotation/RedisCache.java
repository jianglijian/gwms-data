/*
 * RedisCache.java
 *
 * @author bl03846 Created on 2018/1/30 18:05 版本 修改时间 修改内容 V1.0.1 2018/1/30  初始版本
 *
 * Copyright (c) 2007 百世物流科技（中国）有限公司 版权所有CHANGCHUN GENGHIS TECHNOLOGY CO.,LTD. All Rights Reserved.
 */
package com.best.gwms.data.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**    标注是否使用缓存的注解     @author bl03846    @version 1.0.1  */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RedisCache {

    /** 用于绑定的请求参数名字 */
    String value() default "";

    /** 是否必须，默认是 */
    boolean required() default true;

    Class<?> poClass();
}
