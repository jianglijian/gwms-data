package com.best.gwms.data.util;

/** 用来处理时间相关的帮助类 */
public class DateGen {
    private  DateGen() {
    }

    /** 给定月数，计算天，每个月按照30天计算 */
    public static final Long DAYS_OF_ONE_MONTH = 30L;

    /** 给定年数，计算天，每年按照365天计算 */
    public static final Long DAYS_OF_ONE_YEAR = 365L;

    /** 给定年数，计算月份，每年按照12个月计算 */
    public static final Long MONTHS_OF_ONE_YEAR = 12L;

    /** 一天多少秒 */
    public static final Long SECOND_OF_ONE_HOUR = 3600L;

    /** 一小时多少秒 */
    public static final Long SECOND_OF_ONE_MINUTE = 60L;

    public static final String COMMON_DF_4_MONTH = "yyyy-MM";
    public static final String COMMON_DF_4_DAY = "yyyy-MM-dd";
    public static final String COMMON_DF_DD_MM_YYYY = "dd-MM-yyyy";
    public static final String COMMON_DF_DD_MM_YYYYHHmmss = "dd-MM-yyyy HH:mm:ss";
    public static final String COMMON_DF_REVERSE_4_DAY = "MM-dd-yyyy";
    public static final String COMMON_DF_4_DAY_yyyyMMdd = "yyyyMMdd";
    public static final String COMMON_DF_4_DAY_yyMMdd = "yyMMdd";
    public static final String COMMON_DF_4_MONTH_yyyyMM = "yyyyMM";
    public static final String COMMON_DF_4_MONTH_yyMM = "yyMM";
    public static final String COMMON_DF_4_yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String COMMON_DF_4_yyyyMMdd_HHmmss = "yyyyMMdd_HHmmss";     //报表名称导出日期特殊格式要求
    public static final String COMMON_DF_4_yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
    public static final String COMMON_DF_4_yyyyMMddHHmm = "yyyyMMddHHmm";
    public static final String COMMON_DF_4_yyyyMMddHH = "yyyyMMddHH";
    public static final String COMMON_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String COMMON_DF_STR_4_ORACLE = "yyyy-MM-dd HH24:mi:ss";
    public static final String COMMON_DF_STR_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ssZZ";
    public static final String COMMON_HH24mmssSSS = "HH:mm:ss:SSS";

    public static final String COMMON_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String COMMON_DATE_FORMAT_4_DAY = "yyyy-MM-dd";

    public static final String EN_DATE_FORMAT_HH24 = "MM-dd-yyyy HH:mm:ss";
    public static final String EN_DATE_FORMAT = "MM-dd-yyyy hh:mm:ss a";

    public static final String EN_SPRIT_DATE_FORMAT_4_DAY = "MM/dd/yyyy";

    public static final String COMMON_DF_STR_WITH_TIMEZONE_NO_ZZ = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String COMMON_DATE_MILLISECOND_FORMAT_STR = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String COMMON_DF_4_yyMMddHHmmss = "yyMMddHHmmss";
}
