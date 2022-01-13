package com.best.gwms.data.helper;

import com.best.gwms.common.constant.FieldConstant;
import com.best.gwms.data.model.bas.SearchObject;
import com.best.gwms.data.model.bas.WebSearchObject;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

/**
 * Created by BG331163 on 2018/8/15.
 * Version 1.0.1
 */
@Component
public class WebSoConvertHelper<S extends WebSearchObject, T extends SearchObject> implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(WebSoConvertHelper.class);

    @Autowired
    private SpringBeanHelper springBeanHelper;

    public T convert(S webSo, Class<T> targetClass) {
        if (webSo == null) {
            return null;
        }
        Stopwatch stopwatch= Stopwatch.createStarted();
        try {
            // 对于前端传了“”的string 类型的成员，将其值设定为null
            BeanWrapperImpl wrapper = new BeanWrapperImpl(webSo);
            PropertyDescriptor[] descriptors = wrapper.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                if (String.class != descriptor.getPropertyType()) {
                    continue;
                }
                Object value = null;
                try {
                    Method readMethod = descriptor.getReadMethod();
                    if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    value = readMethod.invoke(webSo);
                    if (value == null) {
                        continue;
                    }
                    if (FieldConstant.CONSTANT_BLANK.trim().equalsIgnoreCase(value.toString())) {
                        Method writeMethod = descriptor.getWriteMethod();
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        writeMethod.invoke(webSo, new Object[1]);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    // logger.info("", e);
                    continue;
                }
            }
           logger.info("convert empty string cost:{}ms",stopwatch.elapsed(TimeUnit.MILLISECONDS));
            // 执行属性copy
            T target = targetClass.newInstance();
            springBeanHelper.copyProperties(webSo, target);

            return target;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
