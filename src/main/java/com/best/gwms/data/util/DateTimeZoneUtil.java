package com.best.gwms.data.util;

import com.google.common.collect.Sets;

import java.time.*;
import java.time.chrono.ChronoZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Set;

import static com.best.gwms.data.util.EasyUtil.isStringEmpty;


public class DateTimeZoneUtil {
    public static final String STOCK_SNAP = "STOCK_SNAP";
    public static final String CONSTANT_UNDERLINE = "_";

    private DateTimeZoneUtil() {}

    /**
     * @param timeZone
     * @param endDate 接收到的endDate 减去偏移量就变成了实际仓库的十二点，接收的数据转成date是被转化后
     *     服务器时间转到仓库当地当时的时间，如果仓库当地的时间比选择的时间大则查询的为历史
     * @return
     */
    public static boolean isQueryHis(ZonedDateTime endDate, String timeZone) {
        if (endDate == null) {
            return false;
        }
        endDate = endDate.withZoneSameInstant(ZoneId.of(timeZone));
        // 服务器时间转到仓库当地当时的时间，
        ZonedDateTime whNowTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of(timeZone));
        if (whNowTime == null) {
            return false;
        }
        if (isSameDay(whNowTime, endDate)) {
            return false;
        }


        return whNowTime.isAfter(endDate);
    }

    public static Integer getDiffDay(ZonedDateTime businessDate, ZonedDateTime arrivalDate) {
        if (businessDate.toLocalDate().compareTo(arrivalDate.toLocalDate())==0) {
            return 0;
        }
        return ((int) (Duration.between(arrivalDate, businessDate).toDays()));
    }

    /**
     * 获取库存快照的表名,
     *
     * @param tzBusinessDate
     * @param tzInboundTime 表名规则: CONCAT('RP_DAILY_', current_year, '_',current_month, '_'
     *     ,'STOCK_SNAP');
     * @return
     */
    public static Set<String> getSnapTblName(ZonedDateTime tzBusinessDate, ZonedDateTime tzInboundTime) {
        Integer dayDiff = getDiffDay(tzBusinessDate, tzInboundTime);

        if (dayDiff < 0) {
            return Sets.newHashSet();
        }
        if (dayDiff == 0) {

            int year = tzBusinessDate.getYear();
            int month = tzBusinessDate.getMonthValue();

            return Sets.newHashSet(STOCK_SNAP + year + CONSTANT_UNDERLINE + month);
        }
        Set<String> res = Sets.newHashSet();
        for (int i = dayDiff; i > 0; i--) {
            ZonedDateTime zonedDateTime = tzBusinessDate.minusDays(i);

            int year = zonedDateTime.getYear();
            int month = zonedDateTime.getMonthValue();

            res.add(STOCK_SNAP + year + "_" + month);
        }

        return res;
    }


    /**
    * 判断两个日期是否在同一天
    *
    * @param localDateTime1
    * @param localDateTime2
    * @return
    */
    public static Boolean isSameDay(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {

        if (localDateTime1 != null && localDateTime2 != null) {
            return isSameDay(localDateTime1.toLocalDate(), localDateTime2.toLocalDate());
        }
        return false;
    }

    /**
     * 判断两个日期是否在同一天
     *
     * @param
     * @param
     * @return
     */
    public static Boolean isSameDay(LocalDate localDate1, LocalDate localDate2) {

        if (localDate1 != null && localDate2 != null) {

            return localDate1.isEqual(localDate2);
        }
        return false;
    }

    /**
     * 判断两个日期是否在同一天
     *
     * @param
     * @param
     * @return
     */
    public static Boolean isSameDay(ZonedDateTime zonedDateTime1, ZonedDateTime zonedDateTime2) {

        if (zonedDateTime1 != null && zonedDateTime2 != null) {

            return isSameDay(zonedDateTime1.toLocalDate(), zonedDateTime2.toLocalDate());
        }
        return false;
    }


    /**
     * 判断两个日期是否在同一天
     *
     * @param
     * @param
     * @return
     */
    public static Boolean isSameTime(ZonedDateTime zonedDateTime1, ZonedDateTime zonedDateTime2) {

        if (zonedDateTime1 != null && zonedDateTime2 != null) {

            return isSameDay(zonedDateTime1.toLocalDate(), zonedDateTime2.toLocalDate());
        }
        return false;
    }

    /**
     * 将 yyyy-MM-dd HH24:mi:ss格式的日期字符串转换成ZonedDateTime
     *
     * @param date
     * @param timeZone
     * @return
     */
    public static ZonedDateTime string2ZDT(String date, String timeZone) {
        if (isStringEmpty(date)) {
            return null;
        }
        if (!date.contains(":")) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(DateGen.COMMON_DATE_FORMAT_4_DAY));
            return ZonedDateTime.of(localDate, LocalTime.of(0, 0), ZoneId.of(timeZone));
        }
        return string2ZDT(date, timeZone, DateGen.COMMON_DATE_FORMAT_STR);
    }

    public static ZonedDateTime string2ZDT(String date, String timeZone, String pattern) {
        if (isStringEmpty(date)) {
            return null;
        }
        if (!date.contains(":")) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
            return ZonedDateTime.of(localDate, LocalTime.of(0, 0), ZoneId.of(timeZone));
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of(timeZone));
        return ZonedDateTime.parse(date, dateTimeFormatter);
    }

    /**
     * 将字符串类型的日期按照指定格式进行格式化,并以string类型返回
     * @param date
     * @param timeZone
     * @param
     * @return
     */
    public static String str2Str(String date, String timeZone, DateTimeFormatter formatter) {
        if (isStringEmpty(date) || isStringEmpty(timeZone) || formatter == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = string2ZDT(date, timeZone);
        if (zonedDateTime == null) {
            return null;
        }
        return zonedDateTime.format(formatter);

    }

    /**
     * 将指定格式字符串类型的日期按照指定格式进行格式化,并以string类型返回
     * @param date
     * @param timeZone
     * @param
     * @return
     */
    public static String str2Str(String date, String timeZone, String fromFormatter, DateTimeFormatter formatter) {
        if (isStringEmpty(date) || isStringEmpty(timeZone) || isStringEmpty(fromFormatter ) || formatter == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = string2ZDT(date, timeZone, fromFormatter);
        if (zonedDateTime == null) {
            return null;
        }
        return zonedDateTime.format(formatter);

    }

    /**
     * 将字符串类型的日期按照指定格式进行格式化,并以string类型返回
     * @param date
     * @param timeZone
     * @param
     * @return
     */
    public static String str2Str(String date, String timeZone, String sourceFormatter, String targetFormatter) {
        if (isStringEmpty(date) || isStringEmpty(timeZone) || isStringEmpty(sourceFormatter) || targetFormatter == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = string2ZDT(date, timeZone, sourceFormatter);
        if (zonedDateTime == null) {
            return null;
        }
        return zdt2String(zonedDateTime, timeZone, targetFormatter);
    }

    /**
     * 将字符串类型的日期按照指定格式进行格式化,并以string类型返回
     * @param date
     * @param timeZone
     * @param pattern
     * @return
     */
    public static String str2Str(String date, String timeZone, String pattern) {

        return str2Str(date, timeZone, DateTimeFormatter.ofPattern(pattern));

    }

    /**
     * ZonedDateTime 按指定时区和格式转换成字符串
     * 日期的格式是:yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String zdt2String(ZonedDateTime zonedDateTime, String timeZone) {
        if (zonedDateTime == null) {
            return null;
        }
        return zdt2String(zonedDateTime, timeZone, DateGen.COMMON_DATE_FORMAT_STR);
    }

    /**
     * ZonedDateTime 按指定时区和格式转换成字符串
     *
     * @return
     */
    public static String zdt2String(ZonedDateTime zonedDateTime, String timeZone, String pattern) {
        if (zonedDateTime == null) {
            return null;
        }
        return zdt2String(zonedDateTime, timeZone, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * ZonedDateTime 按指定时区和格式转换成字符串
     *
     * @return
     */
    public static String zdt2String(ZonedDateTime zonedDateTime, String timeZone, DateTimeFormatter formatter) {
        if (zonedDateTime == null) {
            return null;
        }

        return zonedDateTime.withZoneSameInstant(ZoneId.of(timeZone)).format(formatter);
    }

    /**
     * 获取时区所在地的单天时间，没有时分秒
     * @param zonedDateTime
     * @param timeZone
     * @param formatter
     * @return
     */
    public static String zdt2Truncate(ZonedDateTime zonedDateTime, String timeZone, String formatter) {
        if (zonedDateTime == null) {
            return null;
        }
        return zonedDateTime.withZoneSameInstant(ZoneId.of(timeZone)).toLocalDate().format(DateTimeFormatter.ofPattern(formatter));
    }

    /**
     * ZonedDateTime 按指定时区和格式转换成字符串
     *
     * @return
     */
    public static String zdt2StringWithPattern(ZonedDateTime zonedDateTime, String pattern) {
        if (zonedDateTime == null) {
            return null;
        }
        return zonedDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }



    /**
     * ZonedDateTime 按指定时区和格式转换成字符串
     *
     * @return
     */
    public static ZonedDateTime zdtTruncate(ZonedDateTime zonedDateTime, String timeZone) {
        if (zonedDateTime == null) {
            return null;
        }
        LocalDate localDate = zonedDateTime.withZoneSameInstant(ZoneId.of(timeZone)).toLocalDate();
        return ZonedDateTime.of(localDate, LocalTime.of(0, 0), ZoneId.of(timeZone));
    }

    public static Date zdt2Date(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        return Date.from(zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toInstant());
    }

    public static ZonedDateTime date2ZDT(Date date, String timeZone) {
        if (date == null) {
            return null;
        }
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of(timeZone));
    }

    public static ZonedDateTime date2ZDT(Date date, ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        return ZonedDateTime.ofInstant(date.toInstant(), zoneId);
    }

    /**
     * 获取指定时区当前时间
     * @param timeZone
     * @return
     */
    public static ZonedDateTime now(String timeZone) {
        return ZonedDateTime.now(ZoneId.of(timeZone));
    }

    /**
     * 获取指定时区当天时间
     * @param timeZone
     * @return
     */
    public static ZonedDateTime truncateNow(String timeZone) {

        return ZonedDateTime.of(LocalDate.now(ZoneId.of(timeZone)), LocalTime.of(0, 0), ZoneId.of(timeZone));
    }

    /**
     * 获取指定时区当前时间
     * @param timeZone
     * @return
     */
    public static String now(String timeZone, String pattern) {
        return now(timeZone).format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取指定时区当天时间
     * @param timeZone
     * @return
     */
    public static String truncateNow(String timeZone, String pattern) {
        return truncateNow(timeZone).format(DateTimeFormatter.ofPattern(pattern));
    }



    /**
     * 将秒转为字符串，格式为：hh:mi:ss
     *
     * <p>例如：98:23:23 代表：经过了98个小时，23分钟，23秒
     *
     * @param second
     * @return
     */
    public static String parseSecond2Str(Long second) {

        String str = "";

        if (null == second || second < 0) {
            second = 0L;
        }

        Long hour = second / DateGen.SECOND_OF_ONE_HOUR;
        if (0L != hour) {
            if (hour < 10) {
                str += "0";
            }
            str += hour;
            str += ":";
            second = second % DateGen.SECOND_OF_ONE_HOUR;
        } else {
            str += "00:";
        }

        Long minute = second / DateGen.SECOND_OF_ONE_MINUTE;
        if (0L != minute) {
            if (minute < 10) {
                str += "0";
            }
            str += minute;
            str += ":";
            second = second % DateGen.SECOND_OF_ONE_MINUTE;
        } else {
            str += "00:";
        }

        if (0L != second) {
            if (second < 10) {
                str += "0";
            }
            str += second;
        } else {
            str += "00";
        }

        return str;
    }

    /**
     * 把指定时区的时间字符串转换成Date类型
     * @param date
     * @param timeZone
     * @param pattern
     * @return
     */
    public static Date string2Date(String timeZone, String date, String pattern) {
        if (isStringEmpty(date)) {
            return null;
        }
        if (!date.contains(":")) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
            ZonedDateTime time = ZonedDateTime.of(localDate, LocalTime.of(0, 0), ZoneId.of(timeZone));
            return Date.from(time.toInstant());
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of(timeZone));
        ZonedDateTime time = ZonedDateTime.parse(date, dateTimeFormatter);
        return Date.from(time.toInstant());
    }

    /**
     * 把UTC时区的时间字符串转换成Date类型
     * @param date
     * @param pattern
     * @return
     */
    public static Date string2Date(String date, String pattern) {
        if (isStringEmpty(date)) {
            return null;
        }
        return string2Date("UTC", date, pattern);
    }

    /**
     * 获取指定时区的zoneId
     * @param timeZone
     * @return
     */
    public static ZoneId createZoneId(String timeZone) {
        if (isStringEmpty(timeZone)) {
            createZoneId();
        }
        return ZoneId.of(timeZone);
    }

    /**
     * 获取服务器默认的zoneId
     * @return
     */
    public static ZoneId createZoneId() {
        return ZoneId.systemDefault();
    }

    /**
     * LocalDateTime 按指定时区和格式转换成字符串
     *
     * @return
     */
    public static String ldt2StringWithPattern(LocalDateTime localDateTime, String pattern) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * LocalDateTime 按指定格式转换成字符串
     * 日期的格式是:yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String ldt2String(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return ldt2StringWithPattern(localDateTime, DateGen.COMMON_DATE_FORMAT_STR);
    }

    /**
     * LocalDate 按指定格式转换成字符串
     *
     * @return
     */
    public static String ld2StringWithPattern(LocalDate localDate, String pattern) {
        if (localDate == null) {
            return null;
        }
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * LocalDate 按指定格式转换成字符串
     * 日期的格式是:yyyy-MM-dd
     *
     * @return
     */
    public static String ld2String(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return ld2StringWithPattern(localDate, DateGen.COMMON_DATE_FORMAT_4_DAY);
    }

    /**
     * LocalDateTime 按指定格式转换成Date
     * 日期的格式是:yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static Date ldt2Date(LocalDateTime localDateTime, String timeZone) {
        if (localDateTime == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, createZoneId(timeZone));
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 将 yyyy-MM-dd HH24:mi:ss格式的日期字符串转换成LocalDateTime
     *
     * @param date
     * @param 
     * @return
     */
    public static LocalDateTime string2LDT(String date) {
        if (isStringEmpty(date)) {
            return null;
        }
        if (!date.contains(":")) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(DateGen.COMMON_DATE_FORMAT_4_DAY));
            return LocalDateTime.of(localDate, LocalTime.of(0, 0));
        }
        return string2LDT(date, DateGen.COMMON_DATE_FORMAT_STR);
    }

    public static LocalDateTime string2LDT(String date, String pattern) {
        if (isStringEmpty(date)) {
            return null;
        }
        if (!date.contains(":")) {
            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
            return LocalDateTime.of(localDate, LocalTime.of(0, 0));
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(date, dateTimeFormatter);
    }

    /**
     * 将 yyyy-MM-dd格式的日期字符串转换成LocalDate
     *
     * @param date
     * @param
     * @return
     */
    public static LocalDate string2LD(String date) {
        if (isStringEmpty(date)) {
            return null;
        }

        return string2LD(date, DateGen.COMMON_DATE_FORMAT_4_DAY);
    }

    public static LocalDate string2LD(String date, String pattern) {
        if (isStringEmpty(date)) {
            return null;
        }
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     *  ZonedDateTime 转 LocalDateTime
     * @param zonedDateTime
     * @param timeZone
     * @return
     */
    public static LocalDateTime zdt2Ldt(ZonedDateTime zonedDateTime, String timeZone) {
        if (zonedDateTime == null) {
            return null;
        }
        return zonedDateTime.withZoneSameInstant(createZoneId(timeZone)).toLocalDateTime();
    }

    /**
     *  LocalDate 转 ZonedDateTime
     * @param localDate
     * @param timeZone
     * @return
     */
    public static ZonedDateTime ld2Zdt(LocalDate localDate, String timeZone) {
        if (localDate == null) {
            return null;
        }
        ZoneId zoneId = null;
        if (isStringEmpty(timeZone)) {
            zoneId = ZoneId.systemDefault();
        } else {
            zoneId = ZoneId.of(timeZone);
        }
        return ZonedDateTime.of(localDate, LocalTime.of(0, 0), zoneId);
    }

    /**
     * Checks if the instant of this date-time is before that of the specified date-time.
     * <p>
     * This method differs from the comparison in {@link #} in that it
     * only compares the instant of the date-time. This is equivalent to using
     * {@code dateTime1.toInstant().isBefore(dateTime2.toInstant());}.
     * <p>
     * This default implementation performs the comparison based on the epoch-second
     * and nano-of-second.
     *
     * @return true if this point is before the specified date-time
     */
    public static boolean isBefore(ChronoZonedDateTime<?> zonedDateTime1, ChronoZonedDateTime<?> zonedDateTime2) {
        if (zonedDateTime1 != null && zonedDateTime2 != null) {
            return zonedDateTime1.isBefore(zonedDateTime2);
        }

        return false;
    }

    public static boolean isAfter(ChronoZonedDateTime<?> zonedDateTime1, ChronoZonedDateTime<?> zonedDateTime2) {
        if (zonedDateTime1 != null && zonedDateTime2 != null) {
            return zonedDateTime1.isAfter(zonedDateTime2);
        }

        return false;
    }


    /**    需要矫正库存
     * @param timeZone
     * @param endDate 接收到的时间可能为now ，需去时分秒
     * * @return
     */
    public static boolean needVerifySnap(ZonedDateTime endDate, String timeZone) {
        if (endDate == null) {
            return false;
        }
        //到天
        endDate=zdtTruncate(endDate,timeZone);
        // 服务器时间转到仓库当地当时的时间，也到天
        ZonedDateTime whNowTime = truncateNow(timeZone);
        if (whNowTime == null) {
            return false;
        }
        if (isSameDay(whNowTime, endDate)) {
            return false;
        }
        return whNowTime.isAfter(endDate);
    }


    /**
     * 改时间是否是当月
     * @param zonedDateTime
     * @param timeZone
     * @return
     */
    public static boolean isThisMonth(ZonedDateTime zonedDateTime, String timeZone) {
        String checkTime = zdt2String(zonedDateTime, timeZone, DateGen.COMMON_DF_4_MONTH);

        String nowTime = zdt2String(ZonedDateTime.now(), timeZone, DateGen.COMMON_DF_4_MONTH);
        if(nowTime.equals(checkTime)){
            return true;
        }
        return false;
    }



    public static void main(String[] args) {

/*        ZonedDateTime zonedDateTime = DateTimeZoneUtil.truncateNow("US/Eastern");
        String s = zdt2String(zonedDateTime, "US/Eastern", DateGen.COMMON_DATE_FORMAT);
        System.out.println(":::"+s);

        String st = zdt2String(ZonedDateTime.now(), "US/Eastern", DateGen.COMMON_DF_STR_WITH_TIMEZONE_NO_ZZ);
        System.out.println(st);

        ZonedDateTime whNowTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("US/Eastern"));
        System.out.println(whNowTime);

        ZonedDateTime zonedDateTime1 = string2ZDT("2020-03-12 06:34:35","US/Eastern", DateGen.COMMON_DATE_FORMAT_STR);
        System.out.println(zonedDateTime1);

        System.out.println(whNowTime.isAfter(zonedDateTime1));*/

        String start="2020-11-01 23:59:59";
        String end="2020-11-02 23:59:59";

        Set<String> snapTblName = getSnapTblName(DateTimeZoneUtil.string2ZDT(end, "US/Eastern"), DateTimeZoneUtil.string2ZDT(start, "US/Eastern"));
        System.out.println(snapTblName);


        ZonedDateTime endTime = DateTimeZoneUtil.now("US/Eastern");
        ZonedDateTime startTime = endTime.minusDays(30);
        System.out.println("startTime:"+startTime);
        System.out.println("end:"+endTime);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        System.out.println(formatter.format(endTime));

        String now = now("US/Mountain", DateGen.COMMON_DF_4_yyyyMMddHH);
        String hour=now.substring(now.length()-2,now.length());
        System.out.println("now:"+now);
        System.out.println("hour:"+hour);

        System.out.println(truncateNow("US/Mountain"));
        System.out.println(truncateNow("US/Mountain").minusDays(1));
    }


}
