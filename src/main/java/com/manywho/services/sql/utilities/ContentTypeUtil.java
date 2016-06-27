package com.manywho.services.sql.utilities;

import com.manywho.sdk.api.ContentType;
import com.manywho.services.sql.exceptions.DataBaseTypeNotSupported;

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
    public static ContentType createFromSqlType(int types) throws DataBaseTypeNotSupported {
        switch(types) {
            case Types.BIT:
                throw new DataBaseTypeNotSupported("BIT");

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
                throw new DataBaseTypeNotSupported("TIME");

            case Types.TIMESTAMP:

                return ContentType.DateTime;

            case Types.BINARY:
                throw new DataBaseTypeNotSupported("BINARY");

            case Types.VARBINARY:
                throw new DataBaseTypeNotSupported("VARBINARY");

            case Types.LONGVARBINARY:
                throw new DataBaseTypeNotSupported("LONGVARBINARY");

            case Types.NULL:
                throw new DataBaseTypeNotSupported("NULL");

            case Types.OTHER:
                throw new DataBaseTypeNotSupported("OTHER");

            case Types.JAVA_OBJECT:
                throw new DataBaseTypeNotSupported("JAVA_OBJECT");

            case Types.DISTINCT:
                throw new DataBaseTypeNotSupported("DISTINCT");

            case Types.STRUCT:
                throw new DataBaseTypeNotSupported("STRUCT");

            case Types.ARRAY:
                throw new DataBaseTypeNotSupported("ARRAY");

            case Types. BLOB:
                return ContentType.String;

            case Types.CLOB:
                return ContentType.String;

            case Types.REF:
                throw new DataBaseTypeNotSupported("REF");

            case Types.DATALINK:
                throw new DataBaseTypeNotSupported("DATALINK");

            case Types.BOOLEAN:
                return ContentType.Boolean;

            case Types.ROWID:
                throw new DataBaseTypeNotSupported("ROWID");

            case Types.NCHAR:
                return ContentType.String;

            case Types.NVARCHAR:
                return ContentType.String;

            case Types.LONGNVARCHAR:
                return ContentType.String;

            case Types.NCLOB:
                throw new DataBaseTypeNotSupported("NCLOB");

            case Types.SQLXML:
                throw new DataBaseTypeNotSupported("SQLXML");

            case Types.REF_CURSOR:
                throw new DataBaseTypeNotSupported("REF_CURSOR");

            case Types.TIME_WITH_TIMEZONE:
                throw new DataBaseTypeNotSupported("TIME_WITH_TIMEZONE");

            case Types.TIMESTAMP_WITH_TIMEZONE:
                throw new DataBaseTypeNotSupported("TIMESTAMP_WITH_TIMEZONE");

            default:
                throw new DataBaseTypeNotSupported(types + "");

        }
    }
}
