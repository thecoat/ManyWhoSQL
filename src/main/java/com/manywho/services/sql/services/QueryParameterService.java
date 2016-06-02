package com.manywho.services.sql.services;

import com.manywho.services.sql.exceptions.DataBaseTypeNotSupported;
import org.sql2o.Query;
import java.sql.JDBCType;

public class QueryParameterService {
    /**
     * This class need to be updated whith ContentTypeUtil
     *
     *
     * @param paramName
     * @param parameterValue
     * @param databaseType
     * @param query
     * @return
     * @throws DataBaseTypeNotSupported
     */
    public Query addParameterValueToTheQuery(String paramName, String parameterValue, String databaseType, Query query) throws DataBaseTypeNotSupported {
        JDBCType type = JDBCType.valueOf(databaseType);

        switch (type){
            case BIT:
                throw new DataBaseTypeNotSupported("BIT");

            case TINYINT:
                return query.addParameter(paramName, Integer.parseInt(parameterValue));

            case SMALLINT:
                return query.addParameter(paramName, Integer.parseInt(parameterValue));

            case INTEGER:
                return query.addParameter(paramName, Integer.parseInt(parameterValue));

            case BIGINT:
                return query.addParameter(paramName, Integer.parseInt(parameterValue));

            case FLOAT:
                return query.addParameter(paramName, Float.parseFloat(parameterValue));

            case REAL:
                return query.addParameter(paramName, Long.parseLong(parameterValue));

            case DOUBLE:
                return query.addParameter(paramName, Long.parseLong(parameterValue));

            case NUMERIC:
                return query.addParameter(paramName, Long.parseLong(parameterValue));

            case DECIMAL:
                return query.addParameter(paramName, Long.parseLong(parameterValue));

            case CHAR:
                return query.addParameter(paramName, parameterValue);

            case VARCHAR:
                return query.addParameter(paramName, parameterValue);

            case LONGVARCHAR:
                return query.addParameter(paramName, parameterValue);

            case DATE:
                throw new DataBaseTypeNotSupported("BINARY");

            case TIME:
                throw new DataBaseTypeNotSupported("TIME");

            case TIMESTAMP:
                return query.addParameter(paramName, Integer.parseInt(parameterValue));

            case BINARY:
                throw new DataBaseTypeNotSupported("BINARY");

            case VARBINARY:
                throw new DataBaseTypeNotSupported("VARBINARY");

            case LONGVARBINARY:
                throw new DataBaseTypeNotSupported("LONGVARBINARY");

            case NULL:
                throw new DataBaseTypeNotSupported("NULL");

            case OTHER:
                throw new DataBaseTypeNotSupported("OTHER");

            case JAVA_OBJECT:
                throw new DataBaseTypeNotSupported("JAVA_OBJECT");

            case DISTINCT:
                throw new DataBaseTypeNotSupported("DISTINCT");

            case STRUCT:
                throw new DataBaseTypeNotSupported("STRUCT");

            case ARRAY:
                throw new DataBaseTypeNotSupported("ARRAY");

            case BLOB:
                return query.addParameter(paramName, parameterValue);

            case CLOB:
                return query.addParameter(paramName, parameterValue);

            case REF:
                throw new DataBaseTypeNotSupported("REF");

            case DATALINK:
                throw new DataBaseTypeNotSupported("DATALINK");

            case BOOLEAN:
                return query.addParameter(paramName, Boolean.valueOf(parameterValue));

            case ROWID:
                throw new DataBaseTypeNotSupported("ROWID");

            case NCHAR:
                return query.addParameter(paramName, parameterValue);

            case NVARCHAR:
                return query.addParameter(paramName, parameterValue);

            case LONGNVARCHAR:
                return query.addParameter(paramName, parameterValue);

            case NCLOB:
                throw new DataBaseTypeNotSupported("NCLOB");

            case SQLXML:
                throw new DataBaseTypeNotSupported("SQLXML");

            case REF_CURSOR:
                throw new DataBaseTypeNotSupported("REF_CURSOR");

            case TIME_WITH_TIMEZONE:
                throw new DataBaseTypeNotSupported("TIME_WITH_TIMEZONE");

            case TIMESTAMP_WITH_TIMEZONE:
                throw new DataBaseTypeNotSupported("TIMESTAMP_WITH_TIMEZONE");

            default:
                throw new DataBaseTypeNotSupported("");
        }
    }
}
