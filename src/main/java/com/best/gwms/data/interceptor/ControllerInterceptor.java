package com.best.gwms.data.interceptor;

import com.best.gwms.data.constant.FieldConstant;
import com.best.gwms.data.exception.bas.*;
import com.best.gwms.data.exception.code.BizExceptionCode;
import com.best.gwms.data.helper.I18nServiceHelper;
import com.best.gwms.data.model.bas.PackVo;
import com.best.gwms.data.util.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceException;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * @descreption: 统一做web层的异常处理和日志记录
 * @author: Created by maz on 2018/2/6.
 */
@Aspect
@Component
@Order(FieldConstant.FIRST_PRIORITY)
public class ControllerInterceptor {
    public static final Logger logger = LoggerFactory.getLogger(ControllerInterceptor.class);

    @Autowired
    private I18nServiceHelper messageSource;


    @Around("execution(* com.best.gwms.data.controller..*.*(..))")
    public Object interceptor(ProceedingJoinPoint pjp) {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = sra.getRequest();
        HttpSession session = request.getSession();

        String method = pjp.getSignature().getName();
        long start = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        String beginTime = DateTimeZoneUtil.now(TimeZone.getDefault().getID(), DateGen.COMMON_HH24mmssSSS);
        logger.info("method begin:{} begintime:{} UUID:{}", method, beginTime, uuid);

        try {
            Object rlt = pjp.proceed();
            long end = System.currentTimeMillis();
            logger.info("method end:{} beginTime:{} cost:{} UUID:{}", method, beginTime, (end - start), uuid);
            return rlt;
        } catch (Throwable e) {
            e.printStackTrace();
            logger.info("exception e:[{}]",e.getMessage());
            long end = System.currentTimeMillis();
            // 异常统一处理
            Object rlt = parseException(pjp, (Exception) e);
            logger.info("exception method:" + method + " beginTime:" + beginTime + " cost:" + (end - start) + "(ms) " + " UUID:" + uuid);
            return rlt;
        } finally {
            // 清掉threadlocal
            ThreadLocalUtil.refreshThreadLocal();
        }
    }

    private Object parseException(ProceedingJoinPoint pjp, Exception e) {
        PackVo packVo = new PackVo();
        packVo.setSuccess(false);

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = sra.getRequest();
        SessionLocaleResolver slr = (SessionLocaleResolver) RequestContextUtils.getLocaleResolver(request);
        Locale locale = slr.resolveLocale(request);

        MethodSignature method = (MethodSignature) pjp.getSignature();
        Class<?> c = method.getReturnType();
        Object o = null;

        try {
            o = c.newInstance();
            // gwms统一返回packVo
            if (o instanceof PackVo) {
                logger.info("exception class is[{}]", o.getClass());
                if (e instanceof LoginException) {
                    // 登录异常
                    LoginException exception = (LoginException) e;
                    List<BizClientMessage> messages = exception.getClientMessages();
                    localResovle(request, locale, packVo, messages);
                    packVo.setNote(request.getSession().getId());
                    logger.info("LoginException:", e);
                } else if (e instanceof DataBizException) {
                    // 业务异常
                    DataBizException exception = (DataBizException) e;
                    List<BizClientMessage> messages = exception.getClientMessages();
                    if(messages.size()==0){
                        if(e.getCause() instanceof  NullPointerException){
                            packVo.addMsg(BizExceptionCode.NULL_POINT_EXCEPTION, getLocalMessage(locale, BizExceptionCode.NULL_POINT_EXCEPTION, new String[] {BizExceptionCode.NULL_POINT_EXCEPTION}));
                            logger.info("NULL_POINT_EXCEPTION:", e);
                        }
                    }else{
                        localResovle(request, locale, packVo, messages);
                        logger.info("GwbBizException:", e);
                    }
                }else if (e instanceof BizException) {
                    // 业务异常
                    BizException exception = (BizException) e;
                    List<BizClientMessage> messages = exception.getClientMessages();
                    if (messages.size() == 0) {
                        if (e.getCause() instanceof NullPointerException) {
                            packVo.addMsg(BizExceptionCode.NULL_POINT_EXCEPTION, getLocalMessage(locale, BizExceptionCode.NULL_POINT_EXCEPTION, new String[]{BizExceptionCode.NULL_POINT_EXCEPTION}));
                            logger.info("NULL_POINT_EXCEPTION:", e);
                        }
                    } else {
                        packVo.addMsg(BizExceptionCode.MASTER_SERVER_ERROR, getLocalMessage(locale, BizExceptionCode.MASTER_SERVER_ERROR, new String[]{e.getMessage().toString()}));

                        logger.info("GwbBizException:", e);
                    }
                }else if (e instanceof NullPointerException) {
                    // 空指针异常
                    packVo.addMsg(BizExceptionCode.NULL_POINT_EXCEPTION, getLocalMessage(locale, BizExceptionCode.NULL_POINT_EXCEPTION, new String[] {BizExceptionCode.NULL_POINT_EXCEPTION}));
                    logger.info("NULL_POINT_EXCEPTION:", e);
                } else if (e instanceof OptLockException) {
                    // 乐观锁异常
                    packVo.addMsg(BizExceptionCode.OPT_LOCK_EXCEPTION, getLocalMessage(locale, BizExceptionCode.OPT_LOCK_EXCEPTION, new String[] {e.getMessage()}));
                    logger.info("OptLockException:", e);
                } else if (e instanceof org.springframework.dao.DataAccessException) {
                    processDataAccessException(packVo, e, locale);
                    // logger.info("DataAccessException:", e);
                } else if (e instanceof DaoException) {
                    // 数据库异常
                    packVo.addMsg(BizExceptionCode.DAO_EXCEPTION, getLocalMessage(locale, BizExceptionCode.DAO_EXCEPTION, new String[] {e.getMessage()}));
                    logger.info("DaoException:", e);
                } else if (e instanceof WebServiceException) {
                    packVo.addMsg(BizExceptionCode.MASTER_XINGNG_SERVICE_EXCEPTION, getLocalMessage(locale, BizExceptionCode.MASTER_XINGNG_SERVICE_EXCEPTION, new String[] {BizExceptionCode.MASTER_XINGNG_SERVICE_EXCEPTION}));
                } else if (e instanceof IllegalStateException && e.getMessage().equals("No service instance has been discovered.")) {
                    packVo.addMsg(BizExceptionCode.XINGNG_SERVICE_NO_DISCOVERED, getLocalMessage(locale, BizExceptionCode.XINGNG_SERVICE_NO_DISCOVERED, new String[] {e.getMessage()}));
                } else if (e instanceof DataAccessException) {
                    processGWMSDataAccessException(packVo, e, locale);
                } else {
                    // 未知异常
                    packVo.addMsg(BizExceptionCode.UN_KNOWN_EXCEPTION, getLocalMessage(locale, BizExceptionCode.UN_KNOWN_EXCEPTION, new String[] {e.getMessage()}));
                    logger.info("UnkonwnException1:", e);
                }
            } else {
                // 未知异常
                packVo.addMsg(BizExceptionCode.UN_KNOWN_EXCEPTION, getLocalMessage(locale, BizExceptionCode.UN_KNOWN_EXCEPTION, new String[] {e.getMessage()}));
                logger.warn("UnkonwnException2:", e);
            }
        } catch (Exception ex) {
            // 未知异常
            packVo.addMsg(BizExceptionCode.UN_KNOWN_EXCEPTION, getLocalMessage(locale, BizExceptionCode.UN_KNOWN_EXCEPTION, new String[] {e.getMessage()}));
            logger.warn("UnkonwnException3:", e);
        }
        return packVo;
    }

    private void localResovle(HttpServletRequest request, Locale locale, PackVo packVo, List<BizClientMessage> messages) {
        for (BizClientMessage message : messages) {
            packVo.addMsg(message.getCode(), getLocalMessage(locale, message.getCode(), message.getContent()));
        }
    }

    private String getLocalMessage(Locale locale, String code, String[] values) {
        logger.info("parameter:code=[" + code + "] values = [" + values + "]locale=[" + locale + "]");
        try {
            return messageSource.getMessage(code, values , locale);
        } catch (Exception e) {
            logger.error("error code has no configuration.");
            return code;
        }
    }

    public void processDataAccessException(PackVo packVo, Exception dataAccessException, Locale locale) {
        String bizExceptionCode = null;
        if (dataAccessException instanceof DataIntegrityViolationException) {
            bizExceptionCode = SqlExceptionCodeParseUtil.processSQLException(dataAccessException);
            packVo.addMsg(bizExceptionCode, getLocalMessage(locale, bizExceptionCode, new String[] {dataAccessException.getMessage()}));
            logger.error("DataIntegrityViolationException", dataAccessException);
        } else if (dataAccessException instanceof MyBatisSystemException) {
            String msg = dataAccessException.getMessage();
            if (msg.contains("ModifiedByAnotherUserException")) {
                packVo.addMsg(BizExceptionCode.OPT_LOCK_MODIFY_EXCEPTION, getLocalMessage(locale, BizExceptionCode.OPT_LOCK_MODIFY_EXCEPTION, new String[] {dataAccessException.getMessage()}));
            } else if (msg.contains("RemovedByAnotherUserException")) {
                packVo.addMsg(BizExceptionCode.OPT_LOCK_REMOVE_EXCEPTION, getLocalMessage(locale, BizExceptionCode.OPT_LOCK_REMOVE_EXCEPTION, new String[] {dataAccessException.getMessage()}));
            } else {
                packVo.addMsg(BizExceptionCode.DATA_ACCESS_EXCEPTION, getLocalMessage(locale, BizExceptionCode.DATA_ACCESS_EXCEPTION, new String[] {dataAccessException.getMessage()}));
            }
            logger.error("MyBatisSystemException", dataAccessException);
        } else if (dataAccessException instanceof UncategorizedSQLException) {
            bizExceptionCode = SqlExceptionCodeParseUtil.processSQLException(dataAccessException);
            packVo.addMsg(bizExceptionCode, getLocalMessage(locale, bizExceptionCode, new String[] {dataAccessException.getMessage()}));
            UncategorizedSQLException sqlException = (UncategorizedSQLException) dataAccessException;
            logger.error(sqlException.getSql(), sqlException);
        } else if (dataAccessException instanceof BadSqlGrammarException) {
            bizExceptionCode = SqlExceptionCodeParseUtil.processSQLException(dataAccessException);
            packVo.addMsg(bizExceptionCode, getLocalMessage(locale, bizExceptionCode, new String[] {dataAccessException.getMessage()}));
            BadSqlGrammarException sqlException = (BadSqlGrammarException) dataAccessException;
            logger.error(sqlException.getSql(), sqlException);
        } else if (dataAccessException instanceof DeadlockLoserDataAccessException) {
            packVo.addMsg(DAOAccessException.ERR_DEADLOCK, getLocalMessage(locale, DAOAccessException.ERR_DEADLOCK, new String[] {dataAccessException.getMessage()}));
            logger.error("DeadlockLoserDataAccessException", dataAccessException);
        } else {
            packVo.addMsg(DAOAccessException.ERR_DATA_ACCESS_CODE, getLocalMessage(locale, DAOAccessException.ERR_DATA_ACCESS_CODE, new String[] {dataAccessException.getMessage()}));
            logger.error("other DataAccessException", dataAccessException);
        }
    }

    public void processGWMSDataAccessException(PackVo packVo, Exception dataAccessException, Locale locale) {
        DataAccessException masterDataAccessException = (DataAccessException) dataAccessException;
        if (EasyUtil.isStringNotEmpty(masterDataAccessException.getErrorCode())) {
            String bizExceptionCode = masterDataAccessException.getErrorCode();
            // 构建错误信息
            if (bizExceptionCode.equals(BizExceptionCode.OPT_LOCK_MODIFY_EXCEPTION)) {
                packVo.addMsg(BizExceptionCode.OPT_LOCK_MODIFY_EXCEPTION, getLocalMessage(locale, BizExceptionCode.OPT_LOCK_MODIFY_EXCEPTION, new String[] {dataAccessException.getMessage()}));
            } else if (bizExceptionCode.equals(BizExceptionCode.OPT_LOCK_REMOVE_EXCEPTION)) {
                packVo.addMsg(BizExceptionCode.OPT_LOCK_REMOVE_EXCEPTION, getLocalMessage(locale, BizExceptionCode.OPT_LOCK_REMOVE_EXCEPTION, new String[] {dataAccessException.getMessage()}));
            } else if (bizExceptionCode.equals(DAOAccessException.ERR_DEADLOCK)) {
                packVo.addMsg(DAOAccessException.ERR_DEADLOCK, getLocalMessage(locale, DAOAccessException.ERR_DEADLOCK, new String[] {dataAccessException.getMessage()}));
            } else if (bizExceptionCode.equals(DAOAccessException.ERR_SQL_COLUMN_COUNT_NOT_BE_NULL)) {
                // 定义自然语言提醒
                packVo.addMsg(DAOAccessException.ERR_SQL_COLUMN_COUNT_NOT_BE_NULL,
                        getLocalMessage(locale, DAOAccessException.ERR_SQL_COLUMN_COUNT_NOT_BE_NULL, new String[] {dataAccessException.getMessage()}));
            } else if (bizExceptionCode.equals(DAOAccessException.ERR_SQL_UNKNOWN_COLUMN)) {
                packVo.addMsg(DAOAccessException.ERR_SQL_UNKNOWN_COLUMN, getLocalMessage(locale, DAOAccessException.ERR_SQL_UNKNOWN_COLUMN, new String[] {dataAccessException.getMessage()}));
            } else if (bizExceptionCode.equals(DAOAccessException.ERR_SQL_SYNTAX_ERROR)) {
                packVo.addMsg(DAOAccessException.ERR_SQL_SYNTAX_ERROR, getLocalMessage(locale, DAOAccessException.ERR_SQL_SYNTAX_ERROR, new String[] {dataAccessException.getMessage()}));
            } else if (bizExceptionCode.equals(DAOAccessException.ERR_DAO_DATABASE_ERROR_TOO_MANY_CAHARS)) {
                packVo.addMsg(DAOAccessException.ERR_DAO_DATABASE_ERROR_TOO_MANY_CAHARS,
                        getLocalMessage(locale, DAOAccessException.ERR_DAO_DATABASE_ERROR_TOO_MANY_CAHARS, new String[] {dataAccessException.getMessage()}));
            } else if (bizExceptionCode.equals(DAOAccessException.ERR_DAO_DATABASE_ERROR_CODE)) {
                packVo.addMsg(DAOAccessException.ERR_DAO_DATABASE_ERROR_CODE, getLocalMessage(locale, DAOAccessException.ERR_DAO_DATABASE_ERROR_CODE, new String[] {dataAccessException.getMessage()}));
            } else if (bizExceptionCode.equals(DAOAccessException.ERR_TOO_MUCH_CLIENTS)) {
                packVo.addMsg(DAOAccessException.ERR_TOO_MUCH_CLIENTS, getLocalMessage(locale, DAOAccessException.ERR_TOO_MUCH_CLIENTS, new String[] {dataAccessException.getMessage()}));
            } else if (bizExceptionCode.indexOf(FieldConstant.ERR_DATA_UNIQUE) > -1) {
                packVo.addMsg(DAOAccessException.ERR_DATA_UNIQUE, getLocalMessage(locale, DAOAccessException.ERR_DATA_UNIQUE, new String[] {dataAccessException.getMessage()}));
            } else if (bizExceptionCode.equals(DAOAccessException.ERR_DATA_ACCESS_CODE)) {
                packVo.addMsg(DAOAccessException.ERR_DATA_ACCESS_CODE, getLocalMessage(locale, DAOAccessException.ERR_DATA_ACCESS_CODE, new String[] {dataAccessException.getMessage()}));
            }
        }
    }
}
