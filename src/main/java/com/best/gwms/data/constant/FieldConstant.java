package com.best.gwms.data.constant;


import com.best.gwms.data.enums.LangEnum;
import com.best.xingng.service.spring.XingNGSpringLifecyclePhases;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author bl03846
 */
public interface FieldConstant {

    /**
     * gwb相关 begin
     ****/
    //泰国申请面单的时候的系统code 01：GWMS
    String SYSTEM_CODE = "01";
    String CANCEL_REASON = "Order has been cancelled.";
    /**
     * gwb相关 end
     ****/

    String SYSTEM_GWMS = "GWMS";

    Double CHANGE_UNIT = 1000D;

    String CREATE_TIME = "created_time";
    String CREATE_TIME_DESC = "created_time desc";
    String UPDATE_TIME_DESC = "updated_time desc";
    String RESOURCECODE = "resourceCode";
    int LOGIN_MAX_RETRY_TIMES = 5;
    // 修改密码的最大尝试次数
    int CHANGE_PASSWORD_MAX_RETRY_TIMES = 3;
    String RESOURCE_BUTTON = "BUTTON";
    String COLON = ":";
    String COMMA = ",";
    String SPRIT = "/";
    String WEB = "web";
    String LEVEL = "Level";
    String UNDER_LINE = "_";
    String MIDDLE_LINE = "-";
    String status = "status";

    Long DEF_DOMAIN_ID = 10000L;
    String EA = "EA";
    String INP = "INP";
    String CS = "CS";
    String PLT = "PLT";


    String ID = "id";
    String SKU_ID = "skuId";

    String TO_LOC_TYPE_ID = "toLocTypeId";

    String QTY = "qty";

    String MOVE_QTY = "moveQty";



    String LINENO = "lineNo";

    // 默认长度500为一个sql语句
    Integer IN_SQL_LENGTH = 500;

    String GOMS_ATTACHMENT_DOWN_URL = "/attachment/download/anon";

    // sku导入使用常量
    String OWNER = "Owner";
    String OWNER_CODE = "ownerCode";
    String CATEGORY = "Category";
    String SKU = "SKU";
    String SKU_STATUS = "SKU_STATUS";
    String SKU_CONFIG = "SKU_CONFIG";
    String SHELF_LIFE = "Shelf Life";
    String BATTERY = "Battery";
    String YES = "YES";
    String NO = "NO";
    String CLASSIFICATION = "CLASSIFICATION";
    String CLASSIFICATION_A = "A";
    String CLASSIFICATION_B = "B";
    String CLASSIFICATION_C = "C";
    String CLASSIFICATION_D = "D";




    String LOC = "LOC";
    String LPN = "LPN";

    String EMPTY = "Empty";
    String OCCUPIED = "Occupied";

    String ERR_DATA_UNIQUE = "ERR_DAO_UNIQUE_";

    Integer SNAP_DAY = 62;


    String hubBagReport = "Bag Report";
    String hubOrderReport = "Order Report";
    String progress_0 = "0%";
    String progress_100 = "100%";

    String excelExtension = ".xlsx";

    //BOL前缀
    String BOL = "bol";

    //包裹单位,写死
    String PK = "PK";
    /**
     * 每次写入excel文件的记录数量
     */
    Integer excel_count = 3000;

    /**
     * 时间换算进制(60进制)
     */
    Double SEXAGESIMAL = 60D;

    // 限制导最大条数10000
    Integer MAX_EXPORT_COUNT_10000 = 10000;


    //报表导出查询最大数据量
    Integer MAX_EXCEL_SEARCH_COUNT = 20000;

    // 限制数量的查询数据类型
    String EXPORT_LIMIT_COUNT_DATA_TYPE_BOX = "Box";

    // 限制数量的查询数据类型
    String EXPORT_LIMIT_COUNT_DATA_TYPE_TASK_DETAIL = "Task detail";

    //单次label最大打印数量
    Integer MAX_PRINT_LABEL_COUNT = 5000;


    String AUTHC = "authc";

    String SHA1PRNG = "SHA1PRNG";

    String ERROR = "error";


    /**
     * 邮件主题
     */
    String EMAIL_SUBJECT = "GWMS User Identity Verification";



    /**
     * 邮件占位符
     */
    String EMAIL_PLACEHOLDER = "\\?";

    /**
     * 邮件验证码
     */
    String EMAIL_VERIFICATION_CODE = "email_verification_code";

    /**
     * 邮箱地址
     */
    String EMAIL_ADDRESS = "email_address";


    /**
     * 邮箱验证码输错次数上限
     */
    int EMAIL_VERIFICATION_ERROR_LIMIT = 3;

    /**
     * attachMent订单trackingNo的截断的长度
     */
    int SUB_LENGTH = 12;

    /**
     * 尺重保留小数点后2位
     */
    int ROUND_LENGTH = 2;

    String MAIL_SMTP_AUTH = "mail.smtp.auth";

    String MAIL_SMTP_HOST = "mail.smtp.host";

    String MAIL_SMTP_PORT = "mail.smtp.port";

    String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";

    String MAIL_SMTP_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";

    String MAIL_DEBUG = "mail.debug";

    String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";

    String MAIL_SMTP = "smtp";

    String RECEIPT_REPORT_FOLDER = "receipt";
    String RECEIPT_ORDER_FOLDER = "order";
    String SHORTAGE_ORDER_DETAIL_FOLDER = "shortageOrderDetail";
    String TASK_PERFORMANCE_FOLDER = "taskPerformance";
    String ORDER_PARCEL_BOX_FOLDER = "orderParcelBox";
    String STOCK_SHELF_LIFE_FOLDER = "stockShelfLife";
    String ON_TIME_SHIPPING_FOLDER = "onTimeShipping";
    String PERFORMANCE_TRACKER_FOLDER = "performanceTracker";
    String HUB_FOLDER = "hub";

    String ITEM_ACTIVITY_FROM = "Item Activity From:";
    String ITEM_ACTIVITY_TO = "to: ";
    String AM = " AM";
    String PM = " PM";
    String ZERO_STR = " 00:00:00";
    String DATE_TIME_END_STR = " 23:59:59";

    Long ZERO = 0L;
    String ZERO_NUMBER_STR = "0";
    String DEFAULT_TIMEZONE = "UTC";

    String DEFAULT_LANG = LangEnum.en_US.name();
    String DEFAULT_INNER_LANG = LangEnum.zh_CN.name();
    Long DEFAULT_WH = 1L;

    String WHOLE_FORMAT = "WHOLE_FORMAT";
    String DAY_FORMAT = "DAY_FORMAT";

    String STAR_SPLIT = "*";
    String STAR_SPLIT_ESCAPE = "\\*";

    String MULTI_ITEM = "Multi-Item";


    /**
     * 左括号与右括号
     */
    String LEFT_BRACKET = "(";
    String RIGHT_BRACKET = ")";

    /**
     * 左中括号与右中括号
     */
    String LEFT_SQUARE_BRACKET = "[";
    String RIGHT_SQUARE_BRACKET = "]";

    /**
     * 指针
     */
    String FINGER = "->";
    /**
     * 空格
     */
    String SPACE = " ";
    /**
     * 空字符串
     */
    String EMPTY_STRING = "";
    /**
     * 分号
     */
    String SEMICOLON = ";";

    String THREE_POINT_SUFFIX = "...";

    /**
     * 是否启用签收服务     Y表示是，N表示否
     */
    String Y = "Y";
    String N = "N";



    /**
     * 包裹类型
     */
    String PACKAGE_TYPE = "CP";

    String CHINA_COUNTRY = "CN";

    String INVOICE_UNIT = "Each";

    String UPS = "UPS";

    String FEDEX = "FEDEX";

    String DHL = "DHL";

    String APPLYING_FLAG = "ORDER_APPLYING";

    String CONSTANT_BLANK = " ";

    String CONSTANT_SLASH = " / ";

    // 验货信息批量导入 常量
    String BATCH_STATUS = "Status";
    String BATCH_MFG_DATE = "Mfg. Date";
    String BATCH_EXP_DATE = "Exp. Date";
    String BATCH_BATCH = "Batch#";
    String BATCH_COUNTRY_OF_ORIGIN = "Country of Origin";

    String BIN_PREFIX = "Bin ";

    String CANCEL = "Cancel";

    String MASTER_XINGNG_SERVICE_EXCEPTION = "MASTER XINGNG SERVICE EXCEPTION";

    String SUCCESS_CODE = "200";

    String LOCALHOST = "127.0.0.1";

    String MAX_DATE = "9999-12-31";

    /**
     * V2.1 优化版本  定制客户 (国际订单使用WorldShip打印后，订单payTerms需要改为OWNER)
     */
    List<String> specialOwnerCodes = Lists.newArrayList("GENIQUA", "GENIQUA2", "K2MOTOR3");

    String OWNER_WH_RELATION = "OWNER_WH_RELATION";

    String ZONE = "ZONE";

    String SN = "SN";

    Double ZERO_DOUBLE = 0d;
    String ALL = "ALL";

    //用于前端展示
    String ALL_FOR_SHOW = "All";


    String EDIADMIN = "EDIADMIN";

    /**
     * 排序方式
     */
    String ASC = "ASC";
    String DESC = "DESC";

    String DOLLAR = "$";
    String CARET = "^";
    String defaultCutOffTime = "17:00:00";

    String shortDefaultCutOffTime = "17:00";
    String shortDefaultEndShipTime = "23:59:00";
    String excelComment = "The format:[YYYY-MM-DD]";


    /**
     * 30分钟
     */
    int MINUTE_30 = 30;

    /**
     * 边拣边分增加tote最大数量限制
     */
    int MAX_TOTE_COUNT = 5;

    /**
     * 90天
     */
    int DAY_90 = 90;


    /**
     * 来源于gwaybill
     */
    String FROM_GWAYBILL = "from gwaybill";

    String LOCATION_TYPE_RECEIPT = "Receipt";
    String LOCATION_TYPE_QUARANTINE = "Quarantine";
    String LOCATION_TYPE_VARIANCE = "Variance";

    String ERROR_MSG = "ERROR MESSAGE:";
    String SEPARATOR = "|";

    String ADD_OPERATOR = "+";
    String REDUCE_OPERATOR = "-";


    /**
     * 批量更新batch的大小
     */


    /**
     * 默认的域编码
     */
    String DEFAULT_DOMAIN_CODE = "US";
    /**
     * carrier 申请面单2.0接口标志
     */
    String REQUEST_TYPE_CREATE_LABEL = "CreateLabel";


    String US = "US";
    String INCH = "Inch";
    String POUND = "Pound";
    String USA = "U.S.A";
    String PACKAGE_CODE = "packageNumber";
    String TRACKING_NO = "trackingNumber";

    String DOMAIN_CODE = "domainCode";
    Long JOB_DOMAIN = 99999999l;

    /**
     * centurygrow客户编码
     */

    String trueConstant = "true";
    /**
     * 系统用户
     */
    String JOB_USER = "GWMS_SYSTEMHANDLER";

    String CURRENCY_CODE = "USD";
    String VALIDATION_ERROR_CODE = "400";
    String SERVER_ERROR_CODE = "500";
    String SERVER_URL_NOT_EXISTED = "404";
    String WHID = "whId";
    String EMPTY_CONSTANT = "#empty#";
    String OWNER_CONSTANT = "owner";
    String WAREHOUSE_CONSTANT = "warehouse";
    String WAREHOUSE_ORG_TYPE = "WAREHOUSE";
    String OWNER_ORG_TYPE = "OWNER";
    String ORDER_CONSTANT = "order";
    String DOMAIN_CONSTANT = "domain";
    String ABC = "abc";
    String MIXED_ABC = "MIXED";
    String DOC_ID = "docId";
    String FIELD_PRIORITY = "Priority";
    /**
     * 14天
     */
    int DAY_14 = 14;
    String YOUNIQUE = "YOUNIQUE";
    String CONSTANT_FALSE = "FALSE";
    String CONSTANT_TRUE = "TRUE";
    String API = "API";
    String FEEDBACK_RECORD_KEY = "fb_key";
    String CARRIER_SERVICE = "carrierService";
    String SALES_CHANNEL = "salesChannel";
    String SHIPING_LABEL_SOURCE = "shipingLabelSource";
    String UNEQUAL_SIGN = "#";

    String IS = " is ";

    String IS_NOT = " is not ";
    String VS = " vs ";
    /**
     * 优先级
     */
    int FIRST_PRIORITY = 1;
    int SECOND_PRIORITY = 2;

    String CANCELED_KEY = "CANCELED_KEY";
    String UPDATED_KEY = "UPDATED_KEY";
    String FAILED_KEY = "FAILED_KEY";
    String REQUEST_ID = "requestId";
    String PREFIX_W = "W";

    String CONSTANT_WIDTH = " Width";
    String CONSTANT_LENGTH = " Length";
    String CONSTANT_HEIGHT = " Height";
    String CONSTANT_VOLUME = " Volume";
    String CONSTANT_WEIGHT = " Weight";
    String BOX_TRACKING_NO = "trackingNo";
    String BAR_CODE = "barCode";

    String CARRIER_LANDMARK = "LANDMARK";
    String CARRIER_PUROLATOR = "PUROLATOR";

    String KEY_FIELD = "key_field";
    String VALUE_FIELD = "value_field";
    String CREATE_TYPE = "createType";

    String DEFAULT_PACK_TASK_TYPE_CODE = "SWITCHPACK";
    String RESOURCE_URL = "url";

    /**
     * 脱敏信息，出库单查看收件人信息
     */
    String consigneeMsg = "Address1&Name&Phone";
    int TEN = 10;
    String CUT_OFF_PREFIX = "CUT_OFF_";
    Integer CUTOFF_TIME_DEFAULT_PRIORITY = 999;

    String PRODUCTION_ORDER = "Production Order";
    String RECEIPT = "Receipt";


    // 一次向海关推送库存明细的数量
    Integer CUSTOMS_PUSH_STOCK_SIZE = 1000;

    /**
     * 海关报文返回数据状态
     */
    String CUSTOMS_FEED_BACK_SUCCESS = "A001";
    String CUSTOMS_FEED_BACK_FAILED = "A000";
    String PARTNER_ID = "partnerId";
    String PARTNER = "partner";
    String PARTNER_KEY = "partnerKey";
    String SIGN = "sign";
    String SERVICE_TYPE = "serviceType";
    String BIZ_DATA = "bizData";
    String DATA = "data";

    String OUTBOUND_SEND_TO_BUYER = "OUTBOUND_SEND_TO_BUYER";
    String OUTBOUND_RETURN_SELLER = "OUTBOUND_RETURN_SELLER";



    /**
     * excel表导入和导出行数
     */
    Long COST_IMPORT_ROW_COUNT = 20000L;
    Long COST_EXPORT_ROW_COUNT = 5000L;
    int THREE_TIME = 3;
    String MARK_CODE = "markCode";

    String HASH_ALGORITHM_NAME = "md5";

    int HASH_ITERATIONS = 1;

    String LOGIN_URL = "/bas/login";
    String LOGOUT_URL = "/bas/logout";

    String UNAUTHORIZED_URL = "/403";

    String RANDOM_CODE = "verifiCode";

    String SHIRO_LOGIN_SUCCESS = "shiroLoginSuccess";

    String LOGIN_CODE_FIELD = "user";
    String SHIRO_LOGIN_FAILURE = "shiroLoginFailure";
    String RANDOM_CODE_ERROR = "randomCodeError";
    String VERIFICATION_IMG_URL = "/bas/verificationImg";

    String GET_VERSION_CONFIG = "/bas/versionConfig/getOfficialVersionConfig";

    String CHECK_LOGIN_CODE_URL = "/bas/user/checkLoginCode";

    String SEND_EMAIL_URL = "/bas/user/sendEmail";

    /**
     * xingng类加载顺序
     */
    Integer BEST_PHASE = XingNGSpringLifecyclePhases.DISCOVERY;
}
