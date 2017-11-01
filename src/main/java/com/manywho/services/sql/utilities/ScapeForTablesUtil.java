package com.manywho.services.sql.utilities;

import com.manywho.services.sql.entities.DatabaseType;

public class ScapeForTablesUtil {

    public String scapeTableName(DatabaseType databaseType, String schemaNamePrefix, String tableName) {
        String format = "%s.\"%s\"";


        if(databaseType.equals(DatabaseType.Mysql)){
            format = "`%s`.`%s`";
        }

        return String.format(format, schemaNamePrefix, tableName);
    }

    static public String scapeCollumnName(DatabaseType databaseType, String name){
        if(databaseType.equals(DatabaseType.Mysql)){
            return String.format("`%s`", name);
        }else {
            return String.format("\"%s\"", name);
        }
    }
}

