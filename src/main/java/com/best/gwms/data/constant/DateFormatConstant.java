/*
 * DateFormatConstant.java
 *
 * @author bl03846 Created on 2018/8/12 23:05 版本 修改时间 修改内容 V1.0.1 2018/8/12  初始版本
 *
 * Copyright (c) 2007 百世物流科技（中国）有限公司 版权所有CHANGCHUN GENGHIS TECHNOLOGY CO.,LTD. All Rights Reserved.
 */
package com.best.gwms.data.constant;

import com.best.gwms.data.enums.LangEnum;
import com.best.gwms.data.util.DateGen;
import com.best.gwms.data.util.EasyUtil;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.apache.commons.lang.LocaleUtils;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

/**
 *    多语言对应的日期格式
 *
 * @author bl03846
 * @version 1.0.1
 */
public class DateFormatConstant {
    private static final Table<String, String, String> langDateFormatTable = HashBasedTable.create();
    private static final Map<String, Locale> langLocaleMap = Maps.newHashMap();
    static {
        langDateFormatTable.put(LangEnum.en_US.name(), FieldConstant.WHOLE_FORMAT, DateGen.EN_DATE_FORMAT);
        langDateFormatTable.put(LangEnum.en_US.name(), FieldConstant.DAY_FORMAT, DateGen.COMMON_DF_REVERSE_4_DAY);

        langDateFormatTable.put(LangEnum.zh_CN.name(), FieldConstant.WHOLE_FORMAT, DateGen.COMMON_DATE_FORMAT);
        langDateFormatTable.put(LangEnum.zh_CN.name(), FieldConstant.DAY_FORMAT, DateGen.COMMON_DATE_FORMAT_4_DAY);

        langDateFormatTable.put(LangEnum.en_TH.name(), FieldConstant.WHOLE_FORMAT, DateGen.COMMON_DF_DD_MM_YYYYHHmmss);
        langDateFormatTable.put(LangEnum.en_TH.name(), FieldConstant.DAY_FORMAT, DateGen.COMMON_DF_DD_MM_YYYY);

        langDateFormatTable.put(LangEnum.th_TH.name(), FieldConstant.WHOLE_FORMAT, DateGen.COMMON_DF_DD_MM_YYYYHHmmss);
        langDateFormatTable.put(LangEnum.th_TH.name(), FieldConstant.DAY_FORMAT, DateGen.COMMON_DF_DD_MM_YYYY);

        langLocaleMap.put(LangEnum.en_US.name(), Locale.US);
        langLocaleMap.put(LangEnum.zh_CN.name(), Locale.CHINA);
        langLocaleMap.put(LangEnum.th_TH.name(), new Locale("th","TH"));
        langLocaleMap.put(LangEnum.en_TH.name(), LocaleUtils.toLocale(LangEnum.en_TH.name()));
    }

    private DateFormatConstant() {}

    public static Locale getLocal(String lang) {
        if (EasyUtil.isStringEmpty(lang) || LangEnum.getEnum(lang) == null) {
            return langLocaleMap.get(LangEnum.en_US.name());
        }
        LangEnum langEnum = LangEnum.getEnum(lang);
        if (langEnum != null) {
            return langLocaleMap.get(langEnum.name());
        } else {
            return langLocaleMap.get(LangEnum.en_US.name());
        }
    }


    public static DateTimeFormatter getWholeDateFormatter(String lang) {

        return DateTimeFormatter.ofPattern(getWholeDateFormat(lang));
    }
    public static DateTimeFormatter getWholeDateFormatterWithLocale(String lang) {

        return DateTimeFormatter.ofPattern(getWholeDateFormat(lang),getLocal(lang));
    }

    public static DateTimeFormatter getTruncateDateFormatter(String lang) {

        return DateTimeFormatter.ofPattern(getTruncateDateFormat(lang));
    }

    public static String getWholeDateFormat(String lang) {

        return langDateFormatTable.get(LangEnum.getEnum(lang).name(), FieldConstant.WHOLE_FORMAT);
    }

    public static String getTruncateDateFormat(String lang) {

        return langDateFormatTable.get(LangEnum.getEnum(lang).name(), FieldConstant.DAY_FORMAT);
    }
}
