package com.best.gwms.data.util;

import com.google.common.collect.Lists;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @Package : com.best.gwms.common.util @Author : Shen.Ziping[zpshen@best-inc.com] @Date : 2018/1/19
 * 11:36 " @Version : V1.0
 */
public class DozerBeanUtil {
    private static DozerBeanMapper mapper = new DozerBeanMapper();

    static {
        mapper.setMappingFiles(Lists.newArrayList("dozerMapper.xml"));
    }

    private DozerBeanUtil() {}

    @SuppressWarnings("unchecked")
    public static <P> P clone(P base) {
        if (base == null) {
            return null;
        } else {
            return (P) mapper.map(base, base.getClass());
        }
    }

    public static <P> List<P> cloneList(List<P> baseList) {

        if (baseList == null) {
            return Lists.newArrayList();
        } else {
            List<P> targetList = new ArrayList<P>();
            for (P p : baseList) {

                targetList.add(clone(p));
            }
            return targetList;
        }
    }

    public static <V, P> P convert(V base, P target) {

        if (base != null) {
            mapper.map(base, target);
            return target;
        }
        return target;
    }

    public static <V, P> P convert(V base, Class<P> target) {
        if (base == null) {
            return null;
        } else {
            P p = mapper.map(base, target);

            return p;
        }
    }

    public static <V, P> List<P> convertList(List<V> baseList, Class<P> target) {

        if (baseList == null) {
            return Lists.newArrayList();
        } else {
            List<P> targetList = new ArrayList<P>();
            for (V vo : baseList) {

                targetList.add(convert(vo, target));
            }
            return targetList;
        }
    }

    @Deprecated
    public static void copyNotNullProperties(Object source, Object target) throws BeansException {
        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        // 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
                        if (value != null) {
                            Method writeMethod = targetPd.getWriteMethod();
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(target, value);
                        }
                    } catch (Exception ex) {
                        throw new FatalBeanException("Could not copy properties from source to target", ex);
                    }
                }
            }
        }
    }
}
