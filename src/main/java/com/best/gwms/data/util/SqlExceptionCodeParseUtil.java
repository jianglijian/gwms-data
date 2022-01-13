package com.best.gwms.data.util;

import com.best.gwms.data.constant.FieldConstant;
import com.best.gwms.data.exception.bas.DAOAccessException;
import com.best.gwms.data.exception.code.BizExceptionCode;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;

import java.sql.SQLException;

public class SqlExceptionCodeParseUtil {

    public static String processDataAccessException(Exception dataAccessException) {
        String bizExceptionCode = null;
        if (dataAccessException instanceof DataIntegrityViolationException) {
            bizExceptionCode = processSQLException(dataAccessException);
        } else if (dataAccessException instanceof MyBatisSystemException) {
            String msg = dataAccessException.getMessage();
            if (msg.contains("ModifiedByAnotherUserException")) {
                bizExceptionCode = BizExceptionCode.OPT_LOCK_MODIFY_EXCEPTION;
            } else if (msg.contains("RemovedByAnotherUserException")) {
                bizExceptionCode = BizExceptionCode.OPT_LOCK_REMOVE_EXCEPTION;
            }
        } else if (dataAccessException instanceof UncategorizedSQLException) {
            bizExceptionCode = processSQLException(dataAccessException);
        } else if (dataAccessException instanceof BadSqlGrammarException) {
            bizExceptionCode = processSQLException(dataAccessException);
        } else if (dataAccessException instanceof DeadlockLoserDataAccessException) {
            bizExceptionCode = DAOAccessException.ERR_DEADLOCK;
        } else {
            bizExceptionCode = DAOAccessException.ERR_DATA_ACCESS_CODE;
        }
        return bizExceptionCode;
    }

    public static String parseDAOException1088(String errorMessage) {
        // Duplicate entry '39-REC180329001' for key 'UK_WH_RECEIPTCODE'
        int startIndex = errorMessage.indexOf("key '");
        int endIndex = errorMessage.lastIndexOf("'");
        return FieldConstant.ERR_DATA_UNIQUE + errorMessage.substring(startIndex + 5, endIndex);
    }

    public static String processSQLException(Exception dataAccessException) {
        String bizExceptionCode = DAOAccessException.ERR_DATA_ACCESS_CODE;
        if (dataAccessException.getCause() instanceof SQLException) {
            SQLException sqlException = (SQLException) dataAccessException.getCause();
            switch (sqlException.getErrorCode()) {
                case 1048:
                    bizExceptionCode = DAOAccessException.ERR_SQL_COLUMN_COUNT_NOT_BE_NULL;
                    break;
                case 1052:
                case 1054:
                case 1063:
                case 1072:
                    bizExceptionCode = DAOAccessException.ERR_SQL_UNKNOWN_COLUMN;
                    break;
                case 1064:
                    bizExceptionCode = DAOAccessException.ERR_SQL_SYNTAX_ERROR;
                    break;
                case 1406:
                    bizExceptionCode = DAOAccessException.ERR_DAO_DATABASE_ERROR_TOO_MANY_CAHARS;
                    break;
                case 1053:
                case 1077:
                    bizExceptionCode = DAOAccessException.ERR_DAO_DATABASE_ERROR_CODE;
                    break;
                case 1040:
                    bizExceptionCode = DAOAccessException.ERR_TOO_MUCH_CLIENTS;
                    break;
                case 1061:
                case 1062:
                case 1069:
                case 1088:
                    // bizExceptionCode = DAOAccessException.ERR_DAO_UNIQUE_CONSTRAINT_CODE;
                    bizExceptionCode = parseDAOException1088(sqlException.getMessage());
                    break;
                default:
                    break;
            }
        }
        return bizExceptionCode;
    }
}
