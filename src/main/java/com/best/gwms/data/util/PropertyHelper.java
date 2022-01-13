package com.best.gwms.data.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

public class PropertyHelper implements Serializable {
    private static final Log logger = LogFactory.getLog(PropertyHelper.class);

    private PropertyHelper() {}

    public static Object getValue(Object obj, String key) {

        try {
            return PropertyUtils.getNestedProperty(obj, key);
        } catch (Exception e) {

            logger.warn(e);
            return null;
        }
    }

    public static void setValue(Object obj, String key, Object value) {

        try {
            PropertyUtils.setProperty(obj, key, value);
        } catch (Exception e) {

            logger.warn(e);
        }
    }

}
