package com.best.gwms.data.util;

import com.google.common.base.Strings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 数据处理工具类。
 *
 * @author bl00618
 */
public class MathUtil {

    private static final Log logger = LogFactory.getLog(MathUtil.class);

    private static final Double EPS = 1e-10;

    /**
     * 是否为0
     *
     * @param d
     * @return
     */
    public static boolean isZero(Double d) {
        return Double.valueOf(0).equals(d);
    }

    /**
     * 是否为0 或者 null
     *
     * @param d
     * @return
     */
    public static boolean isZeroOrNull(Double d) {
        if (d == null) return true;
        return Double.valueOf(0).equals(d);
    }

    /**
     * 两数相乘
     *
     * @param value1
     * @param value2
     * @return
     */
    public static Double multiply(Object value1, Object value2) {
        if (value1 == null || value2 == null) {
            return Double.valueOf(0);
        }
        BigDecimal val1 = new BigDecimal(value1.toString());
        BigDecimal val2 = new BigDecimal(value2.toString());

        return val1.multiply(val2).doubleValue();
    }

    /**
     * {@code bigger} 是否大于{@code smaller}
     *
     * @param bigger
     * @param smaller
     * @return
     * @author bl05386
     */
    public static boolean biggerThan(Double bigger, Double smaller) {

        bigger = nullToZero(bigger);

        smaller = nullToZero(smaller);

        return bigger.compareTo(smaller) > 0;
    }

    /**
     * 判断{@code bigger}是否小于等于{@code smaller}
     *
     * @param bigger
     * @param smaller
     * @return {@true} {@code bigger} 数值上 小于等于 {@code smaller}
     */
    public static boolean smallerOrEqualThan(Double smaller, Double bigger) {

        smaller = nullToZero(smaller);
        bigger = nullToZero(bigger);

        return smaller.compareTo(bigger) <= 0;
    }

    /**
     * 判断{@code bigger}是否大于等于{@code smaller}
     *
     * @param bigger
     * @param smaller
     * @return {@true} {@code bigger} 数值上 大于等于 {@code smaller}
     */
    public static boolean isBiggerOrEqualThan(Double bigger, Double smaller) {

        bigger = nullToZero(bigger);
        smaller = nullToZero(smaller);

        return bigger.compareTo(smaller) >= 0;
    }

    /**
     * 判断{@code bigger}是否小于等于{@code smaller}
     *
     * @param bigger
     * @param smaller
     * @return {@true} {@code bigger} 数值上 大于等于 {@code smaller}
     */
    public static boolean isSmallerOrEqualThan(Double smaller, Double bigger) {

        bigger = nullToZero(bigger);
        smaller = nullToZero(smaller);

        return smaller.compareTo(bigger) <= 0;
    }

    /**
     * {@code qty} 是否大于0
     *
     * @param qty
     * @return true qty不为空且大于0
     */
    public static boolean isBiggerThanZero(Double qty) {

        if (qty == null) return false;

        return qty.compareTo(0D) > 0;
    }

    /**
     * 是否为负数
     *
     * @param qty
     * @return true 参数不为空且小于0
     */
    public static boolean isNegative(Double qty) {
        if (qty == null) return false;
        return qty.compareTo(0D) < 0;
    }

    /**
     * {@code value1} 是否小于 {@code value2}
     *
     * @param value1
     * @param value2
     * @return
     * @author bl05386
     */
    public static boolean isSmallerThan(Double value1, Double value2) {

        value1 = nullToZero(value1);

        value2 = nullToZero(value2);

        return value1.compareTo(value2) < 0;
    }

    public static boolean isSmallerThanZero(Double value) {

        value = nullToZero(value);

        return value.compareTo(0D) < 0;
    }

    /**
     * 判断给定参数是否小于等于0
     *
     * @param d d是null时，视为0
     * @return
     */
    public static boolean isSmallerOrEqualThanZero(Double d) {

        d = nullToZero(d);

        return d.compareTo(0D) <= 0;
    }

    public static boolean equals(Double value1, Double value2) {
        value1 = nullToZero(value1);
        value2 = nullToZero(value2);
        return value1.equals(value2);
    }

    /**
     * 两数相除
     *
     * @param value
     * @param divisor
     * @param len     保留的小数位
     * @return
     */
    public static Double divide(Object value, Object divisor, int len) {
        if (value == null || divisor == null) {
            return Double.valueOf(0);
        }

        BigDecimal val1 = new BigDecimal(value.toString());
        BigDecimal val2 = new BigDecimal(divisor.toString());

        if (val2.doubleValue() == 0D) {
            logger.error("divisor is null or zero");
            throw new IllegalArgumentException("divisor is null or zero");
        }

        return val1.divide(val2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static Double divide(Object value, Object divisor) {
        return divide(value, divisor, 4);
    }

    /**
     * 根据factor因子结合两个Double数字，factor为 1 则两数相加，为 -1 则两数相减
     *
     * @param first
     * @param second
     * @param factor 1 或 -1；
     * @return
     */
    public static Double mix(Double first, Double second, int factor) {
        if (Math.abs(factor) != 1) {
            throw new IllegalArgumentException("错误的因子！factor只能为1或-1");
        }

        first = first == null ? 0D : first;
        second = second == null ? 0D : second;
        return add(first, second * factor);
    }

    /**
     * Double型加法运算。
     *
     * @param first  nullable
     * @param second nullable
     * @return
     */
    public static Double add(Double first, Double second) {
        BigDecimal sum = BigDecimal.ZERO; // new BigDecimal(0)
        if (first != null) {
            sum = BigDecimal.valueOf(first);
        }
        if (second != null) {
            sum = sum.add(BigDecimal.valueOf(second));
        }
        return sum.doubleValue();
    }

    public static Double add(Double first, Double second, Double third) {
        return add(add(first, second), third);
    }

    /**
     * 求和。
     *
     * <p>null to 0d.
     *
     * @param doubleList
     * @return
     * @author bl05386
     */
    public static Double addList(List<Double> doubleList) {

        Double result = 0d;
        for (Double any : doubleList) {
            result = MathUtil.add(result, any);
        }

        return result;
    }

    /**
     * Double型减法运算。
     *
     * @param first
     * @param second
     * @return
     */
    public static Double sub(Double first, Double second) {
        if (first == null) {
            first = 0D;
        }
        if (second == null) {
            second = 0D;
        }
        return add(first, second * -1);
    }

    public static Double sub(Double first, Double second, Double third) {
        return sub(sub(first, second), third);
    }

    public static Double min(Double first, Double second) {

        Double first1 = nullToZero(first);
        Double second2 = nullToZero(second);

        return MathUtil.sub(first1, second2) > 0 ? second2 : first1;
    }

    public static Double min(Double first, Double second, Double third) {
        return min(third, min(first, second));
    }

    public static Double max(Double first, Double second) {

        Double first1 = nullToZero(first);
        Double second2 = nullToZero(second);

        return MathUtil.sub(first1, second2) > 0 ? first1 : second2;
    }

    public static Double max(Double first, Double second, Double third) {
        return max(third, max(first, second));
    }

    public static Double middle(Double first, Double second, Double third) {
        Double sumValue = add(first, second, third);

        Double maxValue = max(first, second, third);
        Double minValue = min(first, second, third);

        return sub(sumValue, maxValue, minValue);
    }

    private static Double nullToZero(Double d) {
        return d == null ? 0D : d;
    }

    public static Double reverse(Double value) {
        if (value == null) {
            return 0d;
        }
        return -1 * value;
    }

    public static Double abs(Double value) {
        if (value == null) {
            return 0D;
        }
        return Math.abs(value);
    }

    // 四舍五入。size:截取指定长度
    public static Double roundUp(int size, Double number) {
        StringBuffer formatString = new StringBuffer("0");
        if (size > 0) {
            formatString.append(".");
        }
        String numberStr = number.toString();
        int indexDot = numberStr.indexOf(".");
        int numberDot = numberStr.substring(indexDot).length() - 1;
        size = size > numberDot ? numberDot : size;
        for (int i = 0; i < size; i++) {
            formatString.append("#");
        }
        DecimalFormat df = new DecimalFormat(formatString.toString());
        return Double.valueOf(df.format(number));
    }

    /**
     * 四舍五入
     *
     * @param value
     * @param size  保留小数位数
     * @return
     */
    public static Double round(Double value, int size) {
        String pattern = "#0.0";
        if (size > 1) {
            for (int i = 0; i < size - 1; i++) {
                pattern += "0";
            }
        }

        DecimalFormat df = new DecimalFormat(pattern);
        return Double.valueOf(df.format(value));
    }

    /**
     * 判断String是否可以转换为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean canParseToInteger(String str) {
        Pattern pattern = Pattern.compile("^[-]?[\\d]+$");
        return str != null && pattern.matcher(str).matches();
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false 上面的方法貌似无法判断类似1.0的“整数”，所以又写了这个方法 add by zxl 2011-05-12
     */
    public static boolean isInteger2(String str) {
        Pattern pattern = Pattern.compile("(^[0-9]+[.][0]+$)||(^[0-9]+$)");
        return pattern.matcher(str).matches();
    }

    /**
     * 截取trackingNo的判断
     *
     * @param str 传入的字符串
     * @return 是02->09
     */
    public static boolean isSubTracking(String str) {
        Pattern pattern = Pattern.compile("(^[0]+[2-9])");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为Double
     *
     * @param str   输入字符串
     * @return True、False
     */
    public static boolean isDouble(String str){

        try{
            Double.parseDouble(str);
        }catch (Exception e){
            return false;
        }

        return true;
    }

    /**
     * 当字符串的长度超过maxLength时，截取掉前面的benIndex个字符
     */
    public static String subStr4Char(String str, int maxLength, int benIndex) {
        if (str == null || "".equals(str)) {
            return "";
        }
        char[] charArray = str.toCharArray();
        if (charArray.length >= maxLength) {
            return str.substring(benIndex);
        }
        return str;
    }

    public static Integer getIntFromString(String src) {
        if (Strings.isNullOrEmpty(src)) {
            return 0;
        }
        if (isInteger(src)) {
            return Integer.parseInt(src);
        }
        return 0;
    }

    public static boolean equals(Long long1, Long long2) {
        if (long1 == null && long2 == null) {
            return true;
        }
        return long1 != null && long1.equals(long2);
    }

    /**
     * 返回数字的小数位数
     *
     * @param num
     * @return
     */
    public static Integer getNumScale(Double num) {
        String numStr = String.valueOf(num);
        int dotIndex = numStr.indexOf(".");

        if (dotIndex < 0) {
            return 0;
        }

        int numLength = numStr.length();
        return numLength - (dotIndex + 1);
    }

    /**
     * 取模/取余 不能完全保证正确性，最好再调用四舍五入的方法，保证正确性
     *
     * @param first
     * @param second
     * @return
     */
    public static Double mod(Double first, Double second) {
        if (isZeroOrNull(second)) {
            return first;
        }
        BigDecimal b_first = new BigDecimal(first);
        BigDecimal b_second = new BigDecimal(second);
        Float f_value = b_first.remainder(b_second).floatValue();
        return new Double(f_value);
    }

    /**
     * 给定一个Double类型，判断是否为整数
     *
     * @param value
     * @return
     */
    public static boolean doubleIsInt(Double value) {

        if (value == null) return false;

        Double div = sub(value, (double) (value.intValue()));

        return EPS.compareTo(div) > 0;
    }


    /**
     * 获取数据的小数部分,限制长度为两位，不足添加0，超过直接截取掉(不存在为默认的00),
     * 以字符串形式返回
     *
     * @param value 传入的数字
     * @return
     */
    public static String getDecimalPart(Double value) {
        String[] split = value.toString().split("\\.");
        String decimalPart = "00";
        //小数部分不存在则则默认为00，不足两位则填充，超过则截取为两位
        if (split.length > 1) {
            String temp = split[1];
            if (temp.length() < 2) {
                temp = temp + "0";
            }
            if (temp.length() > 2) {
                temp = temp.substring(0, 2);
            }
            decimalPart = temp;
        }
        return decimalPart;
    }

    /**
     * 获取数据的整数部分
     * 以字符串形式返回(包含符号)
     *
     * @param value 传入的数字
     * @return
     */
    public static String getIntegerPart(Double value) {
        String[] split = value.toString().split("\\.");
        return split[0];
    }

    /**
     * 判断字符串是否都是数字
     *
     * @param strNum 传入的字符串
     * @return
     */
    public static boolean isDigit(String strNum) {
        return strNum.matches("[0-9]{1,}");
    }


    /**
     * 四舍五入，固定返回保留两位小数的
     * @param value 传入的字符串
     * @return
     */

    public static double getRoundValue(double value) {
        double roundValue = (double) (Math.round(value * 100)) / 100;
        return roundValue;
    }

    public static void main(String[] args) {
        System.out.println(MathUtil.round(1.345d, 2));

        double d = 1.345d;
        double e = 1.245d;
        double f = 1.145d;
        double g = 1.445d;
        double h = 1.455d;
        double i = 1.355d;
        double j = 1.155d;
        double k = 1.156d;


//  这里的100就是2位小数点,如果要其它位,如4位,这里两个100改成10000
        double b = (double) (Math.round(d * 100)) / 100;
        System.out.println(b);

        double e1 = (double) (Math.round(e * 100)) / 100;
        System.out.println(e1);

        double f1 = (double) (Math.round(f * 100)) / 100;
        System.out.println(f1);

        double g1 = (double) (Math.round(g * 100)) / 100;
        System.out.println(g1);

        double h1 = (double) (Math.round(h * 100)) / 100;
        System.out.println(h1);
        double i1 = (double) (Math.round(i * 100)) / 100;
        System.out.println(i1);
        double j1 = (double) (Math.round(j * 100)) / 100;
        System.out.println(j1);

        double k1 = (double) (Math.round(k * 100)) / 100;
        System.out.println(k1);


        System.out.println("----------------------------");


        String str = "11";

        System.out.println(addZeroForNum(str,0));
        System.out.println(addZeroForNum(str,1));
        System.out.println(addZeroForNum(str,2));
        System.out.println(addZeroForNum(str,3));
        System.out.println(addZeroForNum(str,4));
        System.out.println(addZeroForNum(str,5));
        System.out.println(addZeroForNum(str,6));
        System.out.println(addZeroForNum(str,7));
        System.out.println(addZeroForNum(str,8));
        System.out.println(addZeroForNum(str,9));
    }


    /**
     * 向下取整
     * @param value
     * @return
     */
    public static Double floor(Double value) {
        if (value == null) {
            return Math.floor(0);
        }
        return Math.floor(value);
    }

    /**
     *  补零
     * @param str  需要补零的字符串
     * @param length  补齐之后的位数
     * @return
     */
    public static String addZeroForNum(String str, int length){

        int strLength = str.length();

        while(strLength < length){

            StringBuffer sb = new StringBuffer();

            sb.append("0").append(str);

            str = sb.toString();
            strLength = str.length();
        }

        return str;
    }

}
