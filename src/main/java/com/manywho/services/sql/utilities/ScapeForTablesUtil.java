package com.manywho.services.sql.utilities;

import java.util.Objects;
public class ScapeForTablesUtil {

    public String scapeTableName(String databaseType, String schemaNamePrefix, String tableName) {
        String format = "%s.%s";

        if(Objects.equals(databaseType, "mysql")){
            format = "`%s`.%s";
        }

        return String.format(format, schemaNamePrefix, tableName);
    }
}

