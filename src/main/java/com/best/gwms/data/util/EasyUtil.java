package com.best.gwms.data.util;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** 一个用来转换类型的帮助类 */
public class EasyUtil {

    public static final String DATE_FORMAT = "dd-HH:mm:ss";
    public static final String DF = "yyyy-MM-dd HH:mm:ss";
    public static final String EQUAL_OPERATE = "=";
    public static final String NOT_EQUAL_OPERATE = "!=";
    /** logger */
    private static final Log logger = LogFactory.getLog(EasyUtil.class);

    private static final String LOG_SPLIT = "\u001E";

    private EasyUtil() {
    }

    public static final <T> boolean isNull(T obj, String fieldPath) {
        boolean rlt = true;
        try {
            rlt = PropertyUtils.getNestedProperty(obj, fieldPath) == null;
        } catch (Exception e) {
            logger.warn(e);
        }
        return rlt;
    }

    public static final Long parseLongThrowExp(String s) {
        Long rlt = Longs.tryParse(s);
        if (rlt == null) {
            throw new RuntimeException("数字转换失败,字符串为：" + s);
        }
        return rlt;
    }

    /**
     * 把驼峰的字符串转换成使用下划线，例如locCode转换成loc_code
     *
     * @param field
     * @return
     */
    public static String toHibField(String field) {
        if (StringUtils.isBlank(field)) {
            return "";
        }

        String string = field.replaceAll("[A-Z]", "@$0").replaceAll("@", "_"); // 正则替换
        if (string.startsWith("_")) {
            return string.toLowerCase().substring(1);
        }
        return string.toLowerCase();
    }

    public static final Integer parseIntegerWithoutExp(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return 0;
        }
        Integer rlt = Ints.tryParse(s);
        return rlt == null ? 0 : rlt;
    }

    public static final Long parseLongWithoutExp(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return 0L;
        }
        Long rlt = Longs.tryParse(s);
        return rlt == null ? 0L : rlt;
    }

    public static final Double parseDoubleWithoutExp(String s) {
        if (Strings.isNullOrEmpty(s)) {
            return 0D;
        }
        Double rlt = Doubles.tryParse(s);
        return rlt == null ? 0 : rlt;
    }

    public static final Date parseDateWithoutExp(DateFormat dateFormat, String s) {
        if (isStringEmpty(s)) {
            return new Date();
        }

        Date rlt = new Date();
        try {
            rlt = dateFormat.parse(s);
        } catch (Exception e) {
            logger.warn(e);
        }
        return rlt;
    }

    /**
     * 此方法参数可以是list,set,此方法用以替代isListEmpty方法
     *
     * @param l
     * @return
     */
    public static final boolean isCollectionEmpty(Collection<?> l) {
        return CollectionUtils.isEmpty(l);
    }

    /**
     * 判断ListMultiMap是否为空
     *
     * @param listMultimap
     * @return true 参数为null或者参数中元素为空
     */
    public static final Boolean isListMultiMapEmpty(ListMultimap<?, ?> listMultimap) {
        if (listMultimap == null) {
            return true;
        }
        return listMultimap.isEmpty();
    }

    /**
     * 若c.size=0或者 c=null 返回false 其他返回true
     *
     * @param c
     * @return
     */
    public static final boolean isCollectionNotEmpty(Collection<?> c) {
        return !isCollectionEmpty(c);
    }

    public static final boolean isStringEmpty(String s) {
        return Strings.isNullOrEmpty(s) || Strings.isNullOrEmpty(s.trim());
    }

    public static final boolean isStringNotEmpty(String s) {
        return !isStringEmpty(s);
    }

    public static final boolean isStringEquals(String a, String b) {
        return StringUtils.equals(a, b);
    }

    /**
     * 忽略大小写的严格字符串比较
     *
     * @param a
     * @param b
     * @return
     *     <table>
     * <tr>
     * <th>a\b</th>
     * <th>null</th>
     * <th>notNull</th>
     * </tr>
     * <tr>
     * <th>null</th>
     * <th>true</th>
     * <th>false</th>
     * </tr>
     * <tr>
     * <th>notNull</th>
     * <th>false</th>
     * <th>{@code a.equalsIgnoreCase(b) 的结果}</th>
     * </tr>
     * </table>
     */
    public static final boolean isStringEqualsIgnoreCase(String a, String b) {
        if (a == null && b == null) {
            return true;
        } else if (a != null) {
            return a.equalsIgnoreCase(b);
        } else {
            return false;
        }
    }

    public static final int getStringLength(String s) {
        return (s == null ? 0 : s.length());
    }

    /**
     * 获取传入字符串的字节数，中文2字节
     *
     * @param s
     * @return
     */
    public static final int getStringByteLength(String s) {
        try {
            return s.getBytes("GB2312").length;
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }
        return 0;
    }

    /**
     * 计算百分比，保留两位小数，以字符串形式返回。
     *
     * @param mole 分子
     * @param deno 分母
     * @return
     */
    public static final String calculatePercent(Double mole, Double deno) {
        if (deno == null || deno == 0) {
            return "0.00%";
        }

        double result = (mole / deno) * 100;

        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(result) + "%";
    }

    public static final String calculatePercent(Integer mole, Integer deno) {
        if (deno == null || deno == 0) {
            return "0.00%";
        }

        double result = (mole * 1.0 / deno) * 100;

        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(result) + "%";
    }

    public static final Date parseDate(String dateStr) {
        DateFormat dateFormat = new SimpleDateFormat(DF);
        if (dateStr != null && !"".equals(dateStr.trim())) {
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }

    public static final String dateFormat(Date date) {
        DateFormat dfTime = new SimpleDateFormat(DATE_FORMAT);
        return dfTime.format(date);
    }

    public static final String dateFormat(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 加list中元素按“，”分隔连成一个字符串
     *
     * @param list
     * @return
     */
    public static final String list2Str(List<?> list) {
        return list2Str(list, ',');
    }

    /**
     * tag拼接成字符串
     *
     * @param list
     * @param tag
     * @return
     */
    public static final String list2Str(List<?> list, char tag) {
        StringBuilder sb = new StringBuilder();
        for (Object item : list) {
            if (sb.length() > 0) {
                sb.append(tag);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    public static String list2Str(List<?> list, String tag) {
        StringBuilder sb = new StringBuilder();
        for (Object item : list) {
            if (sb.length() > 0) {
                sb.append(tag);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * Set中元素“，”分隔连成一个字符串
     *
     * @param set
     * @return
     */
    public static final String set2Str(Set<?> set) {
        return set2Str(set, ',');
    }

    public static final String set2Str(Set<?> set, char tag) {
        StringBuilder sb = new StringBuilder();
        for (Object item : set) {
            if (sb.length() > 0) {
                sb.append(tag);
            }
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * 将对象转换成Double。用于SQL取出的数据类型字段处理，因为double 单元测试环境下取出的是BigDecimal，jobss下取出的是Double。
     * 统一转为String再转回Double。
     *
     * @param value
     * @return
     */
    public static final String convertObject2String(Object value) {
        return value != null ? value.toString() : "null";
    }

    public static final Double convertObject2Double(Object value) {
        Double rlt = 0D;
        try {
            rlt = Double.valueOf(value == null ? "0" : value.toString());
        } catch (Exception e) {
            logger.warn(e);
        }
        return rlt;
    }

    public static final Long convertObject2Long(Object value) {
        Long rlt = 0L;
        try {
            rlt = Long.valueOf(value == null ? "0" : value.toString());
        } catch (Exception e) {
            logger.warn(e);
        }
        return rlt;
    }

    public static final Integer convertObject2Integer(Object value) {
        Integer rlt = 0;
        try {
            rlt = Integer.valueOf(value == null ? "0" : value.toString());
        } catch (Exception e) {
            logger.warn(e);
        }
        return rlt;
    }

    public static final List<Long> convertObjectList2LongList(List<Object> valueList) {
        List<Long> rltList = new ArrayList<>();
        for (Object obj : valueList) {
            Long tmp = convertObject2Long(obj);
            rltList.add(tmp);
        }
        return rltList;
    }

    public static final List<Double> convertObjectList2DoubleList(List<Object> valueList) {
        List<Double> rltList = new ArrayList<>();
        for (Object obj : valueList) {
            Double tmp = convertObject2Double(obj);
            rltList.add(tmp);
        }
        return rltList;
    }

    /**
     * 去除一个list中的重复元素
     *
     * @param list
     * @return
     */
    public static <T> List<T> removeDuplicatedItems(List<T> list) {
        Set<T> set = new HashSet<>();

        set.addAll(list);

        list.clear();
        list.addAll(set);

        return list;
    }

    /**
     * 将一个list按照一个限制拆分成若干个小的list
     *
     * @param <T>
     * @param list
     * @param sizeLimit
     * @return
     */
    public static final <T> List<List<T>> splitList(List<T> list, int sizeLimit) {
        List<List<T>> rlt = new ArrayList<>();

        if (list == null || list.size() <= sizeLimit) {
            rlt.add(list);
            return rlt;
        }

        for (int i = 0; i < list.size(); i += sizeLimit) {
            rlt.add(list.subList(i, i + sizeLimit > list.size() ? list.size() : (i + sizeLimit)));
        }

        return rlt;
    }

    /**
     * String的小写转大写
     *
     * @param s
     * @return
     */
    public static final String upperString(String s) {
        if (null == s || EasyUtil.isStringEmpty(s.trim())) {
            return null;
        } else {
            return s.trim().toUpperCase();
        }
    }

    /**
     * String的小写转大写，再拼上“%%”，一般用在like查询
     *
     * @param s
     * @return
     */
    public static final String upperStringLike(String s) {
        if (EasyUtil.isStringEmpty(s)) {
            return null;
        } else {
            return "%" + s.trim().toUpperCase() + "%";
        }
    }

    /**
     * String的小写转大写， 再右边拼接上 "%" ，一般用在like左前缀查询中
     *
     * @param s
     * @return
     */
    public static final String upperStringRightLike(String s) {

        if (EasyUtil.isStringEmpty(s)) {
            return null;
        } else {
            return s.trim().toUpperCase() + "%";
        }
    }

    /**
     * 将这个string的list转成大写
     *
     * @param l
     * @return
     */
    public static final List<String> upperStringList(List<String> l) {
        if (isCollectionEmpty(l)) {
            return l;
        }

        for (int i = 0; i < l.size(); i++) {
            l.set(i, upperString(l.get(i)));
        }
        return l;
    }

    /**
     * 将这个string的list转成大写加%
     *
     * @param l
     * @return
     */
    public static final List<String> upperStringListLike(List<String> l) {
        if (isCollectionEmpty(l)) {
            return l;
        }

        for (int i = 0; i < l.size(); i++) {
            l.set(i, upperStringLike(l.get(i)));
        }
        return l;
    }


    /**
     * 判断2个object是否一致
     *
     * @param a
     * @param b
     * @return
     */
    public static final boolean isObjEqual(Object a, Object b) {
        return Objects.equals(a, b);
    }


    /**
     * 一些通用的打印log的函数
     *
     * @param theirLogger
     * @param whId
     * @param operateName
     * @param methodName
     * @param beginTime
     */
    public static void writeKeyLog(Log theirLogger, Long whId, String operateName, String methodName, Long beginTime) {
        if (whId == null) {
            whId = 0L;
        }
        if (operateName == null) {
            operateName = "";
        }
        theirLogger.info("***********OPERATE:" + operateName + " WAREHOUSE:" + whId + " METHOD:" + methodName + " EXCUTE:" + (System.currentTimeMillis() - beginTime) + "(ms)***********");
    }

    /**
     * 判断字符串中是否只含有一个"."，且不在开头与结尾。
     *
     * @param str
     * @return true: 如果只有一个点号 or false;
     */
    public static boolean containsOneDot(String str) {
        Pattern pattern = Pattern.compile("^[^\\.]+[\\.]{1}[^\\.]+$"); // 正则匹配
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String getCurrentTreadId() {
        return "thread:" + Thread.currentThread().getId() + " ";
    }


    public static boolean executeStringArithmetic(String first, String second, String operator) {
        if (Strings.isNullOrEmpty(first) || Strings.isNullOrEmpty(second) || Strings.isNullOrEmpty(operator)) {
            return false;
        }
        if (operator.equals("=")) {
            return first.equalsIgnoreCase(second);
        } else if (operator.equals("!=")) {
            return !first.equalsIgnoreCase(second);
        }
        return false;
    }

    public static boolean executeDigitArithmetic(Number first, Number second, String operator) {
        if (first == null || second == null || Strings.isNullOrEmpty(operator)) {
            return false;
        }
        int minus = first.intValue() - second.intValue();
        if (operator.equals("=")) {
            return minus == 0;
        } else if (operator.equals(">")) {
            return minus > 0;
        } else if (operator.equals(">=")) {
            return minus >= 0;
        } else if (operator.equals("<=")) {
            return minus <= 0;
        } else if (operator.equals("<")) {
            return minus < 0;
        } else if (operator.equals("!=")) {
            return minus != 0;
        }
        return false;
    }

    public static List<String> extractOperAndNum(String original) {
        if (Strings.isNullOrEmpty(original)) {
            return Lists.newArrayList();
        }
        List<String> result = null;
        int len = original.length();
        int end = 0;
        for (int i = 0; i < len; i++) {
            if (StringUtils.isNumeric(original.charAt(i) + "")) {
                end = i;
                break;
            }
        }
        if (end > 0) {
            result = new ArrayList<>();
            result.add(original.substring(0, end));
            result.add(original.substring(end));
        }
        return result;
    }

    /**
     * ‘，’分割的Long串解析成List
     *
     * @param str
     * @return
     */
    public static List<Long> str2ListLong(String str) {
        List<Long> ret = new ArrayList<>();
        if (StringUtils.isNotBlank(str)) {
            String[] arr = str.split(",");
            for (String s : arr) {
                try {
                    ret.add(parseLongThrowExp(s));
                } catch (Exception e) {
                    logger.error("str2ListLong error : ", e);
                }
            }
        }
        return ret;
    }

    /**
     * ‘，’分割的Long串解析成Set
     *
     * @param str
     * @return
     */
    public static Set<Long> str2SetLong(String str) {

        Set<Long> rlt = new HashSet<>();

        if (EasyUtil.isStringEmpty(str)) {
            return rlt;
        }

        List<String> strings = Splitter.on(",").splitToList(str);
        rlt = strings.stream().map(Long::parseLong).collect(Collectors.toSet());

        return rlt;
    }

    /**
     * ‘，’分割的String串解析成Set
     *
     * @param str
     * @return
     */
    public static Set<String> str2SetString(String str) {
        Set<String> ret = new HashSet<>();
        if (!Strings.isNullOrEmpty(str)) {
            String[] arr = str.split(",");
            for (String s : arr) {
                s = s.trim();
                try {
                    ret.add(s);
                } catch (Exception e) {
                    logger.error("str2SetString error", e);
                }
            }
        }
        return ret;
    }

    /**
     * ‘，’分割的String串解析成Set.不区分中英文
     *
     * @param str
     * @return
     */
    public static Set<String> str2UpperSetStringIgnoreLang(String str) {
        if (isStringNotEmpty(str)) {
            str = str.replace("，",",");
        }
        Set<String> ret = new HashSet<>();
        Set<String> setString = str2SetString(str);
        setString.stream().forEach(string -> {
            if(isStringNotEmpty(string)){
                ret.add(string.toUpperCase());
            }
        });
        return ret;
    }


    /**
     * ‘，’分割的String串解析成List
     *
     * @param str
     * @return
     */
    public static List<String> str2ListString(String str) {
        List<String> ret = new ArrayList<>();
        if (!Strings.isNullOrEmpty(str)) {
            String[] arr = str.split(",");
            for (String s : arr) {
                s = s.trim();
                try {
                    ret.add(s);
                } catch (Exception e) {
                    logger.error("str2ListString error", e);
                }
            }
        }
        return ret;
    }

    public static String producSubwaveRuleConsumTimeLog(Long whId, String waveCode, Long sohNum, long consumTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("[WHID] : ");
        sb.append(whId);
        sb.append(" [SOWAVECODE] : ");
        sb.append(waveCode);
        sb.append(" [SALEEORDERS] : ");
        sb.append(sohNum);
        sb.append(" [CONSUMETIME] : ");
        sb.append(consumTime);
        return sb.toString();
    }

    public static String addSupSingleQuote(List<String> list) {
        if (EasyUtil.isCollectionEmpty(list)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append("'");
            sb.append(str);
            sb.append("'");
            sb.append(",");
        }

        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }

    public static String addSupSingleQuoteDropStaratAndEnd(List<String> list) {
        String str = addSupSingleQuote(list);
        if (StringUtils.isNotBlank(str) && str.length() > 1) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    public static String formatDouble(Double d, int fractionDigits) {
        if (d == null) {
            return "";
        }
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(fractionDigits);
        return format.format(d);
    }

    /**
     * 按照精度fractionDigits格式化数字
     *
     * @param n
     * @param fractionDigits
     * @return
     */
    public static String formatNumber(Number n, int fractionDigits) {
        if (n == null) {
            return "";
        }
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(fractionDigits);
        return format.format(n);
    }

    /**
     * 将数字中最后的零去掉
     *
     * @param number
     * @return
     */
    public static String dropZeroAtNumberBehind(Number number) {
        if (number == null) {
            return "";
        }
        String str = number.toString();
        str = str.replaceAll("0+?$", "");
        str = str.replaceAll("[.]$", "");
        return str;
    }

    // boolean Boolean methods
    // -----------------------------------------------------------------------

    /**
     * Checks if a <code>Boolean</code> value is <code>true</code>, handling <code>null</code> by
     * returning <code>false</code>.
     *
     * <p>
     *
     * <pre>
     *   BooleanUtils.isTrue(Boolean.TRUE)  = true
     *   BooleanUtils.isTrue(Boolean.FALSE) = false
     *   BooleanUtils.isTrue(null)          = false
     * </pre>
     *
     * @param bool the boolean to check, null returns <code>false</code>
     * @return <code>true</code> only if the input is non-null and true
     * @since 2.1
     */
    public static boolean isTrue(Boolean bool) {
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    /**
     * Checks if a <code>Boolean</code> value is <i>not</i> <code>true</code>, handling <code>null
     * </code> by returning <code>true</code>.
     *
     * <p>
     *
     * <pre>
     *   BooleanUtils.isNotTrue(Boolean.TRUE)  = false
     *   BooleanUtils.isNotTrue(Boolean.FALSE) = true
     *   BooleanUtils.isNotTrue(null)          = true
     * </pre>
     *
     * @param bool the boolean to check, null returns <code>true</code>
     * @return <code>true</code> if the input is null or false
     * @since 2.3
     */
    public static boolean isNotTrue(Boolean bool) {
        return !isTrue(bool);
    }

    /**
     * Checks if a <code>Boolean</code> value is <code>false</code>, handling <code>null</code> by
     * returning <code>false</code>.
     *
     * <p>
     *
     * <pre>
     *   BooleanUtils.isFalse(Boolean.TRUE)  = false
     *   BooleanUtils.isFalse(Boolean.FALSE) = true
     *   BooleanUtils.isFalse(null)          = false
     * </pre>
     *
     * @param bool the boolean to check, null returns <code>false</code>
     * @return <code>true</code> only if the input is non-null and false
     * @since 2.1
     */
    public static boolean isFalse(Boolean bool) {
        if (bool == null) {
            return false;
        }
        return !bool.booleanValue();
    }

    /**
     * Checks if a <code>Boolean</code> value is <i>not</i> <code>false</code>, handling <code>null
     * </code> by returning <code>true</code>.
     *
     * <p>
     *
     * <pre>
     *   BooleanUtils.isNotFalse(Boolean.TRUE)  = true
     *   BooleanUtils.isNotFalse(Boolean.FALSE) = false
     *   BooleanUtils.isNotFalse(null)          = true
     * </pre>
     *
     * @param bool the boolean to check, null returns <code>true</code>
     * @return <code>true</code> if the input is null or true
     * @since 2.3
     */
    public static boolean isNotFalse(Boolean bool) {
        return !isFalse(bool);
    }

    /**
     * 将qty累加到map中
     *
     * @param map
     * @param key
     * @author Joyce
     */
    public static <T> void putIntoMapQty(Map<T, Double> map, T key, Double qty) {
        Double value = map.get(key);
        if (null == value) {
            value = 0d;
        }
        value = MathUtil.add(value, qty);
        map.put(key, value);
    }

    /**
     * 字符串分隔，使用'|'分隔
     *
     * @param str
     * @return
     */
    public static List<String> splitStrWithVerticalLine(String str) {

        List<String> result = new ArrayList<>();

        if (isStringEmpty(str)) {
            return result;
        }

        result = Splitter.on("\\|").splitToList(str);

        result = result.stream().filter(s -> !"".equals(s.trim())).collect(Collectors.toList());

        return result;
    }

    /**
     * 字符串用tag分割，返回字符串List
     *
     * @param str
     * @param tag
     * @return
     */
    public static List<String> splitStrWith(String str, char tag) {
        return Splitter.on(tag).splitToList(str);
    }

    /**
     * 字符串用,分割，返回字符串List
     *
     * @param str
     * @return
     */
    public static List<String> splitStr(String str) {
        return splitStrWith(str, ',');
    }

    /**
     * 按照分隔符对字符串从头开始截取，最后的长度不大于maxlength
     *
     * @param original 原始字符串
     * @param maxLength 字符串截取之后的最大长度
     * @param separator 分隔符
     * @return
     */
    public static String subStrBySeparator(String original, int maxLength, String separator) {
        int length = original.length();
        if (length <= maxLength) {
            return original;
        }
        int originalLength = original.length();
        int position = original.indexOf(separator);
        while (position > -1) {
            length = originalLength - position - 1;
            if (length > maxLength && position < originalLength - 1) {
                position = original.indexOf(separator, position + 1);
            } else {
                break;
            }
        }
        return original.substring(originalLength - length);
    }

    /**
     * 计算datas中包含quantity个元素的组合
     *
     * @param quantity
     * @param datas
     * @return
     */
    public static List<Set<String>> permutation(int quantity, List<String> datas) {
        if (quantity <= 0 || quantity >= datas.size()) {
            Set<String> temp = new HashSet<>(datas);
            List<Set<String>> result = new ArrayList<>();
            result.add(temp);
            return result;
        }
        return permutation(quantity, 1, datas, new HashSet<>());
    }

    private static List<Set<String>> permutation(int quantity, int step, List<String> datas, Set<String> prefixs) {
        List<Set<String>> result = new ArrayList<>();
        if (quantity == step) {
            for (int i = 0; i < datas.size(); i++) {
                Set<String> temp = new HashSet<>();
                temp.addAll(prefixs);
                temp.add(datas.get(i));

                result.add(temp);
            }
            return result;
        }
        for (int i = 0; i < datas.size(); i++) {
            Set<String> temp = new HashSet<>();
            temp.addAll(prefixs);
            temp.add(datas.get(i));

            List<Set<String>> ret = permutation(quantity, step + 1, datas.subList(i + 1, datas.size()), temp);
            result.addAll(ret);
        }
        return result;
    }


    /**
     * 判断传入参数是否都为null
     *
     * @param objs
     * @return
     */
    public static Boolean isAllObjectsNull(Object... objs) {
        for (Object obj : objs) {
            if (obj != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断传入参数中是否存在null(如果是空字符串也认为空) 通常用于方法开始的判断
     * @return
     */
    public static Boolean isObjectsExistNull(Object... objs) {
        for (Object obj : objs) {
            if (obj == null) {
                return true;
            }
            if (obj instanceof String) {
                String objStr = (String) obj;
                if (objStr.trim().length() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 记录方法开始的日志
     *
     * @param logger 日志接口
     * @param method 方法名
     * @param objs 方法的参数，奇数位置是参数名，偶数位置是参数值
     */
    public static void logMethodBeginParams(Log logger, String method, Object... objs) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(method).append("] begin. ");
        int i = 0;
        if (objs != null) {
            for (Object obj : objs) {
                i++;
                // 参数名
                if (i % 2 != 0) {
                    sb.append(obj).append(" = ");

                }
                // 值
                else {
                    sb.append(obj).append(" , ");
                }
            }
        }
        logger.info(sb.toString());
    }

    /**
     * 记录方法结束的日志
     *
     * @param logger 日志接口
     * @param method 方法名
     */
    public static void logMethodFinishParams(Log logger, String method) {
        logMethodFinishParams(logger, method, null);
    }

    /**
     * 记录方法结束的日志
     *
     * @param logger 日志接口
     * @param method 方法名
     * @param objs 方法的参数，奇数位置是参数名，偶数位置是参数值
     */
    public static void logMethodFinishParams(Log logger, String method, Object... objs) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(method).append("] finished. ");
        int i = 0;
        if (objs != null) {
            for (Object obj : objs) {
                i++;
                // 参数名
                if (i % 2 != 0) {
                    sb.append(obj).append(" = ");

                }
                // 值
                else {
                    sb.append(obj).append(" , ");
                }
            }
        }
        logger.info(sb.toString());
    }

    /**
     * 为方法统计而生成的日志内容
     *
     * @param methodName 方法名称
     * @param start 开始时间
     * @param finish 结束时间
     * @return
     */
    public static String logMetric(String methodName, long start, long finish) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LOG_SPLIT).append("METRIC_METHOD ").append(LOG_SPLIT).append(LOG_SPLIT).append(methodName).append(LOG_SPLIT).append("=").append(LOG_SPLIT).append((finish - start))
                .append(LOG_SPLIT);

        return stringBuilder.toString();
    }

    /**
     * 为方法统计而生成的日志内容
     *
     * @param methodName 方法名称
     * @param elapseTime 方法执行耗费时间
     *
     * @return
     */
    public static String logMetric(String methodName, long elapseTime) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LOG_SPLIT).append("METRIC_METHOD ").append(LOG_SPLIT).append(LOG_SPLIT).append(methodName).append(LOG_SPLIT).append("=").append(LOG_SPLIT).append(elapseTime)
                .append(LOG_SPLIT);

        return stringBuilder.toString();
    }

    /**
     * 为方法统计而生成的日志内容
     *
     * @param methodName 方法名称
     * @param start 开始时间
     * @param finish 结束时间
     * @return
     */
    public static String logMetricException(String methodName, long start, long finish) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LOG_SPLIT).append("METRIC_METHOD ").append(LOG_SPLIT).append(LOG_SPLIT).append(methodName + "_exception").append(LOG_SPLIT).append("=").append(LOG_SPLIT)
                .append((finish - start)).append(LOG_SPLIT);

        return stringBuilder.toString();
    }

    /**
     * 为菜单使用统计生成的日志内容
     *
     * @param menuName 菜单名称
     * @param operatorId 操作人
     * @param whId 仓库ID
     */
    public static String menuUseLogStr(String menuName, Long operatorId, Long whId) {

        if (EasyUtil.isStringEmpty(menuName)) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LOG_SPLIT).append("METRIC_MENU ").append(LOG_SPLIT).append(LOG_SPLIT).append(menuName).append(LOG_SPLIT).append(" is executed by ").append(LOG_SPLIT).append(operatorId)
                .append(LOG_SPLIT).append(" from ").append(LOG_SPLIT).append(whId).append(LOG_SPLIT);
        return stringBuilder.toString();
    }

    public static final Boolean isStringEqualsIgnoreNull(String a, String b) {
        if (a == null) {
            a = "";
        }
        if (b == null) {
            b = "";
        }
        a = a.trim();
        b = b.trim();
        return a.equals(b);
    }

    /**
     * 去掉list里面每个String的前后空格
     *
     * @param l
     * @return
     */
    public static final List<String> trimStringList(List<String> l) {
        List<String> list = new ArrayList();
        if (EasyUtil.isCollectionEmpty(l)) {
            return l;
        }

        for (String str : l) {
            if (EasyUtil.isStringNotEmpty(str)) {
                list.add(trimString(str));
            }
        }
        return list;
    }

    /**
     * 去掉前后空格
     *
     * @param str
     * @return
     */
    public static final String trimString(String str) {
        if (EasyUtil.isStringEmpty(str)) {
            return "";
        }
        return str.trim();
    }

    /**
     * 往set中添加非null的Long类型的值
     * @param set
     * @param value
     */
    public static final void addSetIfNotNull(Set<Long> set, Long value) {
        if (value != null) {
            set.add(value);
        }
    }


    /**
     * 二级排序
     *
     * @param l1V1      第一级value1
     * @param l1V2      第一级value2
     * @param l2V1      第二级value1
     * @param l2V2      第二级value2
     *
     * @param order     排序规则：ASC（升序），DESC（降序）
     * @param empty     空值控制：NULL_MIN（ 空为最小），NULL_MAX（空位最大）
     *
     * @return 0，>0，<0
     */
    public static int secondarySort(String l1V1, String l1V2, String l2V1, String l2V2, String order, String empty) {

        if (isStringEmpty(l1V1)) {

            if (isStringEmpty(l1V2)) {
                return sortString(l2V1, l2V2, order, empty);
            } else {
                return "NULL_MAX".equals(empty) ? ("ASC".equals(order) ? 1 : -1) : ("ASC".equals(order) ? -1 : 1);
            }
        } else {
            if (isStringEmpty(l1V2)) {
                return "NULL_MAX".equals(empty) ? ("ASC".equals(order) ? -1 : 1) : ("ASC".equals(order) ? 1 : -1);
            } else {

                int rlt = sortString(l1V1, l1V2, order, empty);

                if (0 == rlt) {
                    return sortString(l2V1, l2V2, order, empty);
                } else {
                    return rlt;
                }
            }
        }
    }

    private static int sortString(String s1, String s2, String order, String empty) {

        if (isStringEmpty(s1)) {
            if (isStringEmpty(s2)) {
                return 0;
            } else {
                return "NULL_MAX".equals(empty) ? ("ASC".equals(order) ? 1 : -1) : ("ASC".equals(order) ? -1 : 1);
            }
        } else {
            if (isStringEmpty(s2)) {
                return "NULL_MAX".equals(empty) ? ("ASC".equals(order) ? -1 : 1) : ("ASC".equals(order) ? 1 : -1);
            } else {
                return "ASC".equals(order) ? s1.compareTo(s2) : s2.compareTo(s1);
            }
        }
    }

    public static void main(String[] args) {

        class SortData {
            String index;
            String level1;
            String level2;

            SortData(String index, String level1, String level2) {
                this.index = index;
                this.level1 = level1;
                this.level2 = level2;
            }
        }

        List<SortData> list = new ArrayList<>();
        list.add(new SortData("1", "01", "B"));
        list.add(new SortData("2", "02", "C"));
        list.add(new SortData("3", "02", "A"));

        list.sort((o1, o2) -> secondarySort(o1.level1, o2.level1, o1.level2, o2.level2, "DESC", "NULL_MIN"));

        for (SortData s : list) {
            System.out.println(" index = " + s.index);
        }

        double a = 11.234;
        String str = roundByScale(a, 5);
        System.out.println(str);
    }

    /**
     *  将double类型的数据格式化成字符串
     * @param v
     * @param scale 保留位数
     * @return
     */
    public static String roundByScale(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
        }
        if (scale == 0) {
            return new DecimalFormat("0").format(v);
        }
        StringBuilder formatStr = new StringBuilder("0.");
        for (int i = 0; i < scale; i++) {
            formatStr.append("0");
        }
        return new DecimalFormat(formatStr.toString()).format(v);
    }

    /**
     * 判断该对象是否为null
     *
     * @param
     * @return
     */
    public static Boolean isObjectsNull(Object obj) {

        return obj == null;

    }


}
