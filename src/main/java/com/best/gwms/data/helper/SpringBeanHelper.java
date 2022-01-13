package com.best.gwms.data.helper;

import com.best.gwms.data.basinterface.ITimeZone;
import com.best.gwms.data.model.bas.AbstractPo;
import com.best.gwms.data.model.bas.AbstractVo;
import com.best.gwms.data.model.bas.SearchObject;
import com.best.gwms.data.model.bas.WebSearchObject;
import com.best.gwms.data.util.DateTimeZoneUtil;
import com.best.gwms.data.util.EasyUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.FeatureDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 拷贝对象属性，空字段除外
 */
@Component
public class SpringBeanHelper {
    private static final Logger logger = LoggerFactory.getLogger(SpringBeanHelper.class);

    @Autowired
    ITimeZone iTimeZone;

    private SpringBeanHelper() {
    }

    /**
     * BeanUtils 支持属性类型自动转换的功能，不会抛异常，如果属性一样类型不同，转的结果是null。
     * PropertyUtils 不支持属性类型自动转换的功能,不支持属性类型自动转换的功能
     */
    public void copyProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target);
        if (src instanceof AbstractPo && target instanceof AbstractVo) {
            convertZDT2String(src, target);
        }
        if (src instanceof AbstractVo && target instanceof AbstractPo) {
            convertString2ZDT(src, target);
        }
        if (src instanceof WebSearchObject && target instanceof SearchObject) {
            convertString2ZDT(src, target);
        }
    }

    public void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
        if (src instanceof AbstractPo && target instanceof AbstractVo) {
            convertZDT2String(src, target);
        }
        if (src instanceof AbstractVo && target instanceof AbstractPo) {
            convertString2ZDT(src, target);
        }
        if (src instanceof WebSearchObject && target instanceof SearchObject) {
            convertString2ZDT(src, target);
        }
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = Lists.newArrayList(pds).stream().filter(p -> src.getPropertyValue(p.getName()) == null).map(FeatureDescriptor::getName).collect(Collectors.toSet());

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }


    private void convertString2ZDT(Object vo, Object po) {
        String tz = iTimeZone.getTimeZone();

        BeanWrapperImpl wrapper = new BeanWrapperImpl(po);
        PropertyDescriptor[] descriptors = wrapper.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            if (ZonedDateTime.class != descriptor.getPropertyType()) {
                continue;
            }

            String readMethodName = descriptor.getReadMethod().getName();
            Object value = null;
            try {
                Method readMethod = vo.getClass().getMethod(readMethodName);
                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                }
                value = readMethod.invoke(vo);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.info("", e);
                continue;
            }


            if (value == null || EasyUtil.isStringEmpty((String) value)) {
                continue;
            }

            ZonedDateTime zdtValue = DateTimeZoneUtil.string2ZDT(value.toString(), tz);

            Method targetMethod = null;
            try {
                targetMethod = po.getClass().getMethod(descriptor.getWriteMethod().getName(), ZonedDateTime.class);
                targetMethod.setAccessible(true);
                targetMethod.invoke(po, zdtValue);
            } catch (Exception e) {
                logger.info("", e);

            }
        }
    }

    private void convertZDT2String(Object po, Object vo) {
        String tz = iTimeZone.getTimeZone();
        BeanWrapperImpl wrapper = new BeanWrapperImpl(po);
        PropertyDescriptor[] descriptors = wrapper.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            if (ZonedDateTime.class != descriptor.getPropertyType()) {
                continue;
            }
            Method readMethod = descriptor.getReadMethod();
            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                readMethod.setAccessible(true);
            }
            Object value = null;
            try {
                value = readMethod.invoke(po);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.info("", e);

                continue;
            }

            if (value == null) {
                continue;
            }

            String strValue = DateTimeZoneUtil.zdt2String((ZonedDateTime) value, tz);

            Method targetMethod = null;
            try {
                targetMethod = vo.getClass().getMethod(descriptor.getWriteMethod().getName(), String.class);
                targetMethod.setAccessible(true);
                targetMethod.invoke(vo, strValue);
            } catch (Exception e) {
                logger.info("", e);

            }
        }
    }
}
