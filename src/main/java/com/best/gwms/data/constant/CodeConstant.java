package com.best.gwms.data.constant;

public class CodeConstant {
    /*语言     */
    public static final String CCC_BAS_LANGUAGE = "bas_language";
    public static final String CIC_BAS_LANGUAGE_EN_US = "en_US";
    public static final String CIC_BAS_LANGUAGE_ZH_CN = "zh_CN";

    public static final String CCC_SORT_TYPE = "ABC_SORT_TYPE";
    public static final String CIC_SORT_IQ = "IQ";
    public static final String CIC_SORT_IK = "IK";
    public static final String CIC_SORT_IV = "IV";


    public static final String CCC_ANALYZE_METHOD = "ABC_ANALYZE_METHOD";
    public static final String CIC_ANALYZE_OB_QTY = "OB_QTY";
    public static final String CIC_ANALYZE_SKU_QTY = "SKU_QTY";
    public static final String CIC_ANALYZE_SKU_RANK = "SKU_RANK";


    public static final String CCC_ORDER_STAGE = "ORDER_STAGE"; // 发货阶段
    public static final String CIC_ORDER_STAGE_DUE_OUT = "DUE_OUT"; // 新建阶段
    public static final String CIC_ORDER_STAGE_PARTIALLY_ALLOCATED = "PARTIALLY_ALLOCATED"; // 部分分配
    public static final String CIC_ORDER_STAGE_ALLOCATED = "ALLOCATED"; // 分配完成
    public static final String CIC_ORDER_STAGE_PICK = "PICK"; // 生成拣货任务
    public static final String CIC_ORDER_STAGE_PICKING = "PICKING"; // 拣货中
    public static final String CIC_ORDER_STAGE_PICKED = "PICKED"; // 拣货完成
    public static final String CIC_ORDER_STAGE_PACK = "PACK"; // 生成打包任务
    public static final String CIC_ORDER_STAGE_PACKING = "PACKING"; // 打包中
    public static final String CIC_ORDER_STAGE_ROUTING = "ROUTING"; // 路线选择中
    public static final String CIC_ORDER_STAGE_PACKED = "PACKED"; // 打包完成
    public static final String CIC_ORDER_STAGE_SHIPPING = "SHIPPING"; // 发货中
    public static final String CIC_ORDER_STAGE_SHIPPED = "SHIPPED"; // 已发货

    private CodeConstant() {
    }

}





