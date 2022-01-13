package com.best.gwms.data.exception.code;

/**
 * @Package : com.best.gwms.common.exception @Author : Shen.Ziping[zpshen@best-inc.com] @Date :
 * 2018/1/15 9:51 " @Version : V1.0
 */
public class BizExceptionCode {
    /** 通过xingng服务调用的主服务调用失败 */
    public static final String MASTER_XINGNG_SERVICE_EXCEPTION = "MASTER_XINGNG_SERVICE_EXCEPTION";
    /** 通过xingng调用的服务不存在 */
    public static final String XINGNG_SERVICE_NO_DISCOVERED = "XINGNG_SERVICE_NO_DISCOVERED";
    // 数据导入缺少必填列
    public static final String ERR_IMPORT_DATA_LOST_REQUIRE_CLOUM = "ERR_IMPORT_DATA_LOST_REQUIRE_CLOUM";
    /** 异常消息按实体区分，需要到各个实体对应的异常消息中添加对应的CODE,以提高CODE的利用率，避免重复 */

    // EXCEL 文件下载异常
    public static final String ERR_BAS_EXCEL_DOWNLOAD_EXCEPTION = "ERR_BAS_EXCEL_DOWNLOAD_EXCEPTION";

    // SHEET的格式异常
    public static final String ERR_BAS_SHEET_FORMAT = "ERR_BAS_SHEET_FORMAT";

    // 为分类异常
    public static final String SYS_ERR_COMMON = "SYS_ERR_COMMON";

    // XING服务异常
    public static final String SYS_ERR_XING_SERVICE_FAIL = "SYS_ERR_XING_SERVICE_FAIL";

    // 数据库异常
    public static final String DATA_ACCESS_EXCEPTION = "DATA_ACCESS_EXCEPTION";

    // DAO异常
    public static final String DAO_EXCEPTION = "DAO_EXCEPTION";

    // 乐观锁异常
    public static final String OPT_LOCK_EXCEPTION = "OPT_LOCK_EXCEPTION";

    // 乐观锁异常
    public static final String OPT_LOCK_MODIFY_EXCEPTION = "OPT_LOCK_MODIFY_EXCEPTION";

    public static final String OPT_LOCK_REMOVE_EXCEPTION = "OPT_LOCK_REMOVE_EXCEPTION";

    // 未知异常
    public static final String UN_KNOWN_EXCEPTION = "UN_KNOWN_EXCEPTION";

    // 空指针异常
    public static final String NULL_POINT_EXCEPTION = "NULL_POINT_EXCEPTION";

    // 文件导入类型不支持
    public static final String ERR_IMPORT_TYPE_NOT_SUPPORTED = "ERR_IMPORT_TYPE_NOT_SUPPORTED";

    // 导入数据内容异常
    public static final String ERR_IMPORT_DATA_ILLEGAL = "ERR_IMPORT_DATA_ILLEGAL";

    // 数据导入异常
    public static final String ERR_EXPORT_DATA = "ERR_EXPORT_DATA";
    // 对象不存在
    public static final String ERR_INSTANCE_NOT_EXIST = "ERR_INSTANCE_NOT_EXIST";
    // 参数[]缺失
    public static final String ERR_PARAM_REQUIRED = "ERR_PARAM_REQUIRED";
    // 参数[]缺失
    public static final String ERR_CONTROLLER_PARAM_PARSE = "ERR_CONTROLLER_PARAM_PARSE";
    // 时间转换错误（时区转换）
    public static final String ERR_DATE_CONVERT = "ERR_DATE_CONVERT";
    // 上传文件大小超过最大限度
    public static final String ERR_BAS_UPLOAD_EXCEEDS_MAXSIZE = "ERR_BAS_UPLOAD_EXCEEDS_MAXSIZE";
    public static final String ERR_REQUIRE_SKU = "ERR_REQUIRE_SKU";
    public static final String ERR_REQUIRE_ORIGIN_PACKAGING = "ERR_REQUIRE_ORIGIN_PACKAGING";
    public static final String ERR_REQUIRE_ORIGIN_EXPECTED_QTY = "ERR_REQUIRE_ORIGIN_EXPECTED_QTY";
    public static final String ERR_REQUIRE_ID = "ERR_REQUIRE_ID";
    public static final String ERR_QEQUIRE_WHID = "ERR_QEQUIRE_WHID";
    public static final String ERR_REQUIRE_LPN = "ERR_REQUIRE_LPN";
    public static final String ERR_REQUIRE_USERID = "ERR_REQUIRE_USERID";
    public static final String ERR_REQUIRE_TASK_TYPE = "ERR_REQUIRE_TASK_TYPE";
    public static final String ERR_REQUIRE_PRIORITY = "ERR_REQUIRE_PRIORITY";
    public static final String ERR_REQUIRE_OWNERID = "ERR_REQUIRE_OWNERID";
    public static final String ERR_DEVICE_NOT_SUPPORT = "ERR_DEVICE_NOT_SUPPORT";
    public static final String ERR_REQUIRE_RECEIPTCODE = "ERR_REQUIRE_RECEIPTCODE";
    public static final String ERR_COMMON_ERR = "ERR_COMMON_ERR";
    /** 基础数据[codeClass]类型[codeInfo]维护不正确 */
    public static final String ERR_CODE_INFO_NOT_EXISTS = "ERR_CODE_INFO_NOT_EXISTS";
    /** 快照表还没生成，查询时候提醒的异常信息 */
    public static final String ERR_SNAP_TABLE_NOT_EXISTS = "ERR_SNAP_TABLE_NOT_EXISTS";

    public static final String ERR_COULD_NOT_UPDATE_SEQ_RULE = "ERR_COULD_NOT_UPDATE_SEQ_RULE";
    public static final String ERR_DASHBOARD_SETTING_TOO_MORE = "ERR_DASHBOARD_SETTING_TOO_MORE";
    public static final String ERR_MISS_PAGINATION_PARAMETER = "ERR_MISS_PAGINATION_PARAMETER";
    public static final String GWB_EXCEPTION = "GWB_EXCEPTION";

    // 主服务异常
    public static final String MASTER_SERVER_ERROR = "MASTER_SERVER_ERROR";
    private BizExceptionCode() {}
}
