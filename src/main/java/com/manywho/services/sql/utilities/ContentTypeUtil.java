package com.manywho.services.sql.utilities;

import com.manywho.sdk.api.ContentType;
import java.sql.Types;

public class ContentTypeUtil {


    /**
     * Return the com.manywho.sdk.apy.ContentType for a java.sql.Types it will throw an exception if the type is not
     * supported
     *
     * @param  int types
     * @return ContentType
     * @throws Exception
     */
    public static ContentType createFromSqlType(int types) throws Exception {
        switch(types) {
            case Types.BIT:
                return ContentType.Number;

            case Types.TINYINT:
                return ContentType.Number;

            case Types.SMALLINT:
                return ContentType.Number;

            case Types.INTEGER:
                return ContentType.Number;

            case Types.BIGINT:
                return ContentType.Number;

            case Types.FLOAT:
                return ContentType.Number;

            case Types.REAL:
                return ContentType.Number;

            case Types.DOUBLE:
                return ContentType.Number;

            case Types.NUMERIC:
                return ContentType.Number;

            case Types.DECIMAL:
                return ContentType.Number;

            case Types.CHAR:
                return ContentType.String;

            case Types.VARCHAR:
                return ContentType.String;

            case Types.LONGVARCHAR:
                return ContentType.String;

            case Types.DATE:
                return ContentType.DateTime;

            case Types.TIME:
                throw new Exception("database type TIME not supported");

            case Types.TIMESTAMP:
                return ContentType.Number;

            case Types.BINARY:
                return ContentType.Number;

            case Types.VARBINARY:
                return ContentType.Number;

            case Types.LONGVARBINARY:
                return ContentType.Number;

            case Types.NULL:
                throw new Exception("database type NULL not supported");

            case Types.OTHER:
                throw new Exception("database type OTHER not supported");

            case Types.JAVA_OBJECT:
                throw new Exception("database type JAVA_OBJECT not supported");

            case Types.DISTINCT:
                throw new Exception("database type DISTINCT not supported");

            case Types.STRUCT:
                throw new Exception("database type STRUCT not supported");

            case Types.ARRAY:
                throw new Exception("database type ARRAY not supported");

            case Types. BLOB:
                return ContentType.String;

            case Types.CLOB:
                return ContentType.String;

            case Types.REF:
                throw new Exception("database type REF not supported");

            case Types.DATALINK:
                throw new Exception("database type DATALINK not supported");

            case Types.BOOLEAN:
                return ContentType.Boolean;

            case Types.ROWID:
                throw new Exception("database type ROWID not supported");

            case Types.NCHAR:
                return ContentType.String;

            case Types.NVARCHAR:
                return ContentType.String;

            case Types.LONGNVARCHAR:
                return ContentType.String;

            case Types.NCLOB:
                throw new Exception("database type NCLOB not supported");

            case Types.SQLXML:
                throw new Exception("database type SQLXML not supported");

            case Types.REF_CURSOR:
                throw new Exception("database type REF_CURSOR not supported");

            case Types.TIME_WITH_TIMEZONE:
                return ContentType.DateTime;

            case Types.TIMESTAMP_WITH_TIMEZONE:
                return ContentType.DateTime;

            default:
                throw new Exception("database type with code "+ types +" not supported");

        }
    }
}
