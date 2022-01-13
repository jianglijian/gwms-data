package com.best.gwms.data.exception.bas;


public class DAOAccessException extends DataBizException {

    private static final long serialVersionUID = -4487429038874269500L;

    public static final String ERR_DAO_UNIQUE_UK_WH_RECEIPTCODE = "ERR_DAO_UNIQUE_UK_WH_RECEIPTCODE";

    public static final String ERR_DAO_UNIQUE_UNQ_WH_LOCCODE = "ERR_DAO_UNIQUE_UNQ_WH_LOCCODE";
    public static final String ERR_DAO_UNIQUE_UK_WH_ZONECODE = "ERR_DAO_UNIQUE_UK_WH_ZONECODE";
    public static final String ERR_DAO_UNIQUE_UK_WH_CONFIGKEY = "ERR_DAO_UNIQUE_UK_WH_CONFIGKEY";
    public static final String ERR_DAO_UNIQUE_UK_LPNCODE_RELEASE = "ERR_DAO_UNIQUE_UK_LPNCODE_RELEASE";
    public static final String ERR_DAO_UNIQUE_UQ_OH_CODE = "ERR_DAO_UNIQUE_UQ_OH_CODE";
    public static final String ERR_DAO_UNIQUE_UNQ_WAVE_CODE = "ERR_DAO_UNIQUE_UNQ_WAVE_CODE";
    public static final String ERR_DAO_UNIQUE_UK_TH_WH_TASK_CODE = "ERR_DAO_UNIQUE_UK_TH_WH_TASK_CODE";
    public static final String ERR_DAO_UNIQUE_UQ_WHID_CODE = "ERR_DAO_UNIQUE_UQ_WHID_CODE";
    public static final String ERR_DAO_UNIQUE_UQ_WH_USER_TYPE = "ERR_DAO_UNIQUE_UQ_WH_USER_TYPE";

    /** 传入参数不合理! */
    public static final String ERR_SQL_UNKNOWN_COLUMN = "ERR_SQL_UNKNOWN_COLUMN";

    /** 输入字段过长，请重新输入！ */
    public static final String ERR_DAO_DATABASE_ERROR_TOO_MANY_CAHARS = "ERR_DAO_DATABASE_ERROR_TOO_MANY_CAHARS";

    /** 数据库异常 */
    public static final String ERR_DAO_DATABASE_ERROR_CODE = "ERR_DAO_DATABASE_ERROR_CODE";

    /** 正在使用中,无法删除! */
    public static final String ERR_DAO_REMOVE_INUSED_CODE = "ERR_DAO_REMOVE_INUSED_CODE";

    /** 数据库最多支持1000条的数据查询! */
    public static final String ERR_DAO_IN_MORE_THAN_1000 = "ERR_DAO_IN_MORE_THAN_1000";

    /**
     * PC端使用，请勿改名
     *
     * <p>已存在相同记录（唯一性约束错误），请检查数据!
     */
    public static final String ERR_DAO_UNIQUE_CONSTRAINT_CODE = "ERR_DAO_UNIQUE_CONSTRAINT_CODE";

    /** 数据处理失败! */
    public static final String ERR_DATA_ACCESS_CODE = "ERR_DATA_ACCESS_CODE";
    /** 连接数过多 */
    public static final String ERR_TOO_MUCH_CLIENTS = "ERR_TOO_MUCH_CLIENTS";
    /** 列不能为空 */
    public static final String ERR_SQL_COLUMN_COUNT_NOT_BE_NULL = "ERR_SQL_COLUMN_COUNT_NOT_BE_NULL";
    /** 死锁 */
    public static final String ERR_DEADLOCK = "ERR_DEADLOCK";
    /** 数据库唯一键约束 */
    public static final String ERR_DATA_UNIQUE = "ERR_DATA_UNIQUE";

    /** sql 语法错误 */
    public static final String ERR_SQL_SYNTAX_ERROR = "ERR_SQL_SYNTAX_ERROR";

    public DAOAccessException(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
