/*
 * MapperHelper.java
 *
 * @author bl03846 Created on 2016/12/28 16:49 版本 修改时间 修改内容 V1.0.1 2016/12/28  初始版本
 *
 * Copyright (c) 2007 百世物流科技（中国）有限公司 版权所有CHANGCHUN GENGHIS TECHNOLOGY CO.,LTD. All Rights Reserved.
 */
package com.best.gwms.data.helper;

import org.apache.commons.lang.StringUtils;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**    类的描述信息       @author bl03846    @version 1.0.1  */
public class MapperHelper {
    private static List<String> excludeFidldList = new ArrayList<String>();
    private String mybatisPoClassPath = "com.best.oasis.wms.biz.salesOrder.model.MbSalesOrderHeader" + "\n";
    private String tableName = "GV_OWNERSHIP_TRANSFER" + "\n";
    private String nameSpace = "";
    Boolean extendLotFlag = false;

    public MapperHelper(String mybatisPoClassPath, String tableName, String nameSpace) {
        this.mybatisPoClassPath = mybatisPoClassPath;
        this.tableName = tableName;
        this.nameSpace = nameSpace;
    }

    @SuppressWarnings("unused")
    private static String getMybatisClassPath(String hibernateClassPath, String mybatisPoClassName) {
        int lastDotIndex = hibernateClassPath.lastIndexOf(".");
        hibernateClassPath = hibernateClassPath.substring(0, lastDotIndex + 1) + mybatisPoClassName;
        return hibernateClassPath.replace("genidc", "wms");
    }

    private static void printLine(String printString) {
        System.out.println(printString);
    }

    private static void printWhiteLine() {
        System.out.println();
    }

    private static boolean isContinueField(Field fld) {
        boolean isContinueField = false;
        String fieldName = fld.getName();
        if (excludeFidldList.contains(fieldName.toUpperCase())) {
            isContinueField = true;
        }

        Transient transientAnnotation = fld.getAnnotation(Transient.class);
        if (transientAnnotation != null) {
            isContinueField = true;
        }
        return isContinueField;
    }

    private static String lastValidField(Field[] fieldlist) {
        String lastValidField = "" + "\n";
        for (int i = 0; i < fieldlist.length; i++) {
            if (!isContinueField(fieldlist[i])) {
                lastValidField = fieldlist[i].getName();
            }
        }
        return lastValidField;
    }

    private String deleteSql(Class<?> cls) {
        String content = "" + "\n";
        content += "<!-- delete Po -->" + "\n";
        content += "<delete id=\"deletePo\" parameterType=\"long\">" + "\n";
        content += "    <include refid=\"com.best.gwms.data.BaseMapper.deletePo\"/>" + "\n";
        content += "</delete>" + "\n";
        return content;
    }

    private String getPoByIdSql(Class<?> cls) {
        String content = "" + "\n";
        content += "<!-- get Po by id-->" + "\n";
        content += "<select id=\"getPoById\" resultMap=\"resultMap\" parameterType=\"long\">" + "\n";
        content += "    <include refid=\"com.best.gwms.data.BaseMapper.getPoById\"/>" + "\n";
        content += "</select>" + "\n";
        return content;
    }

    private String getPoByIdOutMem(Class<?> cls) {
        String content = "" + "\n";
        content += "<!-- get Po by id out mem-->" + "\n" + "\n";
        content += "<select id=\"getPoByIdOutMem\" resultMap=\"resultMap\" parameterType=\"long\">" + "\n" + "\n";
        content += "    <include refid=\"com.best.oasis.wms.biz.system.model.BaseMapper.getPoById\"/>" + "\n" + "\n";
        content += "</select>" + "\n" + "\n";
        return content;
    }

    private String getPoByIdListSql(Class<?> cls) {
        String content = "" + "\n";
        content += "<!-- get Po by idList-->" + "\n";
        content += "<select id=\"getPoByIdList\" resultMap=\"resultMap\">" + "\n";
        content += "    <include refid=\"com.best.gwms.data.BaseMapper.getPoByIdList\"/>" + "\n";
        content += "</select>" + "\n";
        content += "<!-- get Po by idSet-->" + "\n";
        content += "<select id=\"getPoByIdSet\" resultMap=\"resultMap\">" + "\n";
        content += "    <include refid=\"com.best.gwms.data.BaseMapper.getPoByIdSet\"/>" + "\n";
        content += "</select>" + "\n";
        content += "<!-- delete Po by idList-->" + "\n";
        content += "<delete id=\"deletePoList\" parameterType=\"java.util.List\">" + "\n";
        content += "    <include refid=\"com.best.gwms.data.BaseMapper.deletePoList\"/>" + "\n";
        content += "</delete>" + "\n";
        return content;
    }

    private String searchPo() {
        String content = "" + "\n";
        content += "<!-- search Po -->" + "\n";
        content += "<select id=\"searchPo\" resultMap=\"resultMap\">" + "\n";
        content += "    select o.* from" + "\n";
        content += "    <include refid=\"tableSection\" />" + " o" + "\n";
        content += "    <include refid=\"selectSection\" />" + "\n";
        // content +=" <include refid=\"com.best.gwms.data.BaseMapper.orderByCommonSection\" />"+ "\n";
        content += "</select>" + "\n";
        return content;
    }

    private String searchPoCount() {
        String content = "" + "\n";
        content += "<!-- search Po count-->" + "\n";
        content += "<select id=\"countResult\" resultType=\"java.lang.Long\" >" + "\n";
        content += "    select count(o.id) from" + "\n";
        content += "    <include refid=\"tableSection\" />" + "\n";
        content += "    o" + "\n";
        content += "    <include refid=\"selectSection\" />" + "\n";
        content += "</select>" + "\n";
        return content;
    }

    private String generalSearchWhereClause() {
        String content = "" + "\n";
        content += "<!-- general search clause-->" + "\n";
        content += "<sql id=\"selectSection\">" + "\n";
        content += "   <!-- common -->" + "\n";
        // content +=" <include refid=\"com.best.gwms.data.BaseMapper.searchPoFromCommonSection\"/>"+
        // "\n";
        printWhiteLine();
        content += "   <!-- please join other table here if necessary-->" + "\n";
        printWhiteLine();
        printWhiteLine();
        printWhiteLine();
        content += "   <where>" + "\n";
        // content +=" <include refid=\"com.best.gwms.data.BaseMapper.searchPoWhereJoinSection\"/>"+
        // "\n";
        content += "         <include refid=\"com.best.gwms.data.BaseMapper.searchPoWhereCommonSection\"/>" + "\n";
        printWhiteLine();
        content += "         <!-- please add other clause here if necessary-->" + "\n";
        printWhiteLine();
        printWhiteLine();
        printWhiteLine();
        printWhiteLine();
        content += "   </where>" + "\n";
        content += "</sql>" + "\n";
        return content;
    }

    public String createMapper() throws ClassNotFoundException {

        String res = "";

        Class<?> cls = Class.forName(mybatisPoClassPath);
        excludeFidldList.add("SERIALVERSIONUID");

        res += "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "\n" + "\n";
        res += "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >" + "\n" + "\n";
        printWhiteLine();
        // namespace
        res += "<mapper namespace=\"" + nameSpace + "\">" + "\n" + "\n";
        // tableSection
        res += "<sql id=\"tableSection\">" + tableName.toLowerCase() + "</sql>" + "\n" + "\n";
        printWhiteLine();
        printWhiteLine();
        // poTypeSection
        // res += "<sql id=\"poTypeSection\">" + mybatisPoClassPath + "</sql>" + "\n"+ "\n";
        printWhiteLine();

        extendLotFlag =false;// AbstractBatchPo.class.isAssignableFrom(cls);
        printWhiteLine();
        res += insertSql(cls); // insert segment

        printWhiteLine();
        res += updateSql(cls); // update segment

        printWhiteLine();
        res += resultMap(mybatisPoClassPath, cls);

        printWhiteLine();
        res += generalSearchWhereClause();

        printWhiteLine();
        res += searchPo();

        printWhiteLine();
        res += searchPoCount();

        printWhiteLine();
        res += deleteSql(cls); // delete segment

        printWhiteLine();
        res += getPoByIdSql(cls);

        printWhiteLine();
        res += getPoByIdListSql(cls);

        printWhiteLine();
        printWhiteLine();
        printWhiteLine();
        printWhiteLine();
        res += "</mapper>" + "\n" + "\n";

        return res;
    }

    private String insertSql(Class<?> cls) {

        String content = "" + "\n";
        content += "<!-- Create Po -->" + "\n";
        content += "<insert id=\"createPo\" useGeneratedKeys=\"true\" keyColumn=\"id\" keyProperty=\"id\">" + "\n";
        content += "  insert into <include refid=\"tableSection\"/> (" + "\n";
        if (extendLotFlag) {
            content += "      <include refid=\"com.best.gwms.data.BaseMapper.abstractBatchColumn4Create\"/>" + "\n";
        } else {
            content += "      <include refid=\"com.best.gwms.data.BaseMapper.abstractPoColumn4Create\"/>" + "\n";
        }

        Field[] fieldlist = cls.getDeclaredFields();
        int baseFieldCount = 12;

        int j = 0;
        String lastValidField = lastValidField(fieldlist);
        for (int i = 0; i < fieldlist.length; i++) {
            Field fld = fieldlist[i];
            if (isContinueField(fld)) {
                continue;
            }

            String fieldName = fld.getName();
            int index = baseFieldCount + j;
            j++;
            String fieldIndex = "      <!--" + index + "-->";
           // WmsColumn WmsColumnAnnotation = fld.getAnnotation(WmsColumn.class);
         /*   if (WmsColumnAnnotation != null) {
                fieldName = WmsColumnAnnotation.name();
            }*/
            if (!fieldName.equals(lastValidField)) {
                fieldName = fieldName + "," + "\n";
            }
            fieldName = toHibField(fieldName);
            content += "      " + fieldIndex + fieldName;
        }
        content += "      )" + "\n";
        content += "      values (" + "\n";
        if (extendLotFlag) {
            content += "           <include refid=\"com.best.gwms.data.BaseMapper.abstractBatchValue4Create\"/>" + "\n";
        } else {
            content += "           <include refid=\"com.best.gwms.data.BaseMapper.abstractPoValue4Create\"/>" + "\n";
        }

        // 列出mybatis po的字段名和字段类型
        // <!-- 36-->#{roleId,jdbcType=DECIMAL},
        j = 0;
        for (int i = 0; i < fieldlist.length; i++) {
            Field fld = fieldlist[i];
            if (isContinueField(fld)) {
                continue;
            }

            String fieldName = fld.getName();
            int index = baseFieldCount + j;
            j++;
            String fieldIndex = "           <!--" + index + "-->";
            String oracleFieldType = getMySqlFieldType(fld);
            if (!fieldName.equals(lastValidField)) {
                oracleFieldType = oracleFieldType + "},";
            } else {
                oracleFieldType = oracleFieldType + "}";
            }
            content += fieldIndex + "#{" + fieldName + ",jdbcType=" + oracleFieldType + "\n";
        }
        content += "      )" + "\n";
        content += "</insert>" + "\n";
        return content;
    }

    private String updateSql(Class<?> cls) {
        String content = "" + "\n";
        content += "<!-- update Po -->" + "\n" + "\n";
        content += "<update id=\"updatePo\">" + "\n" + "\n";
        content += "  update <include refid=\"tableSection\"/>" + "\n" + "\n";
        content += "  set" + "\n" + "\n";

        if (extendLotFlag) {
            content += "   <include refid=\"com.best.gwms.data.BaseMapper.abstractBatchValue4Update\"/>" + "\n";
        } else {
            content += "  <include refid=\"com.best.gwms.data.BaseMapper.abstractPoColumn4Update\"/>" + "\n" + "\n";
        }

        Field[] fieldlist = cls.getDeclaredFields();
        int baseFieldCount = 12;
        // <!-- 23-->MENUCODE = #{menuCode,jdbcType=VARCHAR},
        int j = 0;
        String lastValidField = lastValidField(fieldlist);
        for (int i = 0; i < fieldlist.length; i++) {
            Field fld = fieldlist[i];
            if (isContinueField(fld)) {
                continue;
            }

            String fieldName = fld.getName();
            int index = baseFieldCount + j;
            j++;
            String fieldIndex = "      <!--" + index + "-->";
           // WmsColumn WmsColumnAnnotation = fld.getAnnotation(WmsColumn.class);
            String mysqlFieldName = "";
            String mybatisPoFieldName = fieldName;
          /*  if (WmsColumnAnnotation != null) {
                mysqlFieldName = WmsColumnAnnotation.name().toUpperCase();
            } else {*/
                mysqlFieldName = toHibField(fieldName);
           // }

            String mysqlFieldType = getMySqlFieldType(fld);
            if (!fieldName.equals(lastValidField)) {
                mysqlFieldType = mysqlFieldType + "},";
            } else {
                mysqlFieldType = mysqlFieldType + "}";
            }
            content += fieldIndex + mysqlFieldName + " = #{" + mybatisPoFieldName + ",jdbcType=" + mysqlFieldType + "\n";
        }
        content += "  where ID = #{id,jdbcType=BIGINT} and lock_version = #{lockVersion,jdbcType=BIGINT}" + "\n" + "\n";
        content += "</update>" + "\n" + "\n";
        return content;
    }

    private String resultMap(String mybatisPoClassPath, Class<?> cls) {
        String content = "" + "\n" + "\n";
        content += "<!-- result map-->" + "\n";

        if (extendLotFlag) {
            content += "<resultMap id=\"resultMap\" type=\"" + mybatisPoClassPath + "\" extends=\"com.best.gwms.data.BaseMapper.abstractBatchResultMap\">" + "\n";
        } else {
            content += "<resultMap id=\"resultMap\" type=\"" + mybatisPoClassPath + "\" extends=\"com.best.gwms.data.BaseMapper.abstractPoResultMap\">" + "\n";
        }

        Field[] fieldlist = cls.getDeclaredFields();
        // <result column="TREEPATH" jdbcType="VARCHAR" property="treePath" />
        int baseFieldCount = 12;
        int j = 0;
        for (int i = 0; i < fieldlist.length; i++) {
            Field fld = fieldlist[i];
            if (isContinueField(fld)) {
                continue;
            }

            String fieldName = fld.getName();
            int index = baseFieldCount + j;
            j++;
            String fieldIndex = "      <!--" + index + "-->" + "\n";
//            WmsColumn WmsColumnAnnotation = fld.getAnnotation(WmsColumn.class);
            String oracleFieldName = "" + "\n";
            String mybatisPoFieldName = fieldName;
//            if (WmsColumnAnnotation != null) {
//                oracleFieldName = WmsColumnAnnotation.name().toUpperCase();
//            } else {
                oracleFieldName = toHibField(fieldName);
           // }
            String oracleFieldType = getMySqlFieldType(fld);
            content += fieldIndex + "<result column=\"" + oracleFieldName + "\" jdbcType=\"" + oracleFieldType + "\" property=\"" + mybatisPoFieldName + "\" />" + "\n";
        }
        content += "</resultMap>" + "\n";
        return content;
    }

    private static String getMySqlFieldType(Field fld) {
        String mysqlFieldType = "";
        // if (fld.getAnnotation(ManyToOne.class) != null) {
        // oracleFieldType = "DECIMAL"+ "\n";
        // }

        String fieldType = fld.getType().toString();
        if ("class java.lang.Long".equals(fieldType)) {
            mysqlFieldType = "BIGINT";
        } else if ("class java.lang.String".equals(fieldType)) {
            mysqlFieldType = "VARCHAR";
        } else if ("class java.lang.Double".equals(fieldType)) {
            mysqlFieldType = "DOUBLE";
        } else if ("class java.time.ZonedDateTime".equals(fieldType)) {
            mysqlFieldType = "DATE";
        } else if ("class java.lang.Boolean".equals(fieldType)) {
            mysqlFieldType = "BIT";
        } else if ("class java.lang.Integer".equals(fieldType)) {
            mysqlFieldType = "INTEGER";
        } else if ("int".equals(fieldType)) {
            mysqlFieldType = "INTEGER";
        } else if ("boolean".equals(fieldType)) {
            mysqlFieldType = "BIT";
        } else if ("BigDecimal".equals(fieldType)) {
            mysqlFieldType = "DECIMAL";
        } else if ("byte".equals(fieldType)) {
            mysqlFieldType = "BIT";
        }
        return mysqlFieldType;
    }

    private static String toHibField(String field) {
        if (StringUtils.isBlank(field)) {
            return "" + "\n";
        }

        String string = field.replaceAll("[A-Z]", " $0").replaceAll(" ", "_"); // 正则替换
        return string.toLowerCase();
    }
}
