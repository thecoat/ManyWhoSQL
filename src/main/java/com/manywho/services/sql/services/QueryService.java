package com.manywho.services.sql.services;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.sql.entities.TableMetadata;

import javax.inject.Inject;

public class QueryService {
    @Inject
    public QueryService() {}

    public String createQueryWithParametersForSelectByPrimaryKey(TableMetadata tableMetadata, String primaryKeyParamName) {
        String sqlFormat = "SELECT * FROM %s.%s WHERE %s=:%s";

        return String.format(sqlFormat, tableMetadata.getSchemaName(),tableMetadata.getTableName(),
                tableMetadata.getPrimaryKeyName(), primaryKeyParamName);
    }

    public String createQueryWithParametersForSelectBySearch(TableMetadata tableMetadata, String search) {
        String sql = "SELECT * FROM %s.%s WHERE %s";
        return String.format(sql, tableMetadata.getSchemaName(), tableMetadata.getTableName(), search);
    }

    public String createQueryWithParametersForUpdate(String paramSuffix, MObject mObject, TableMetadata tableMetadata){

        String sqlFormat = "UPDATE %s.%s SET %s WHERE %s=%s%s";
        String columnsAndParams = "";

        Boolean firstParam = true;

        for(Property p : mObject.getProperties()) {
            if(firstParam) {
                firstParam = false;
            } else {
                columnsAndParams += ", ";
            }

            columnsAndParams += p.getDeveloperName()+ "=:" + paramSuffix + p.getDeveloperName();
        }

        return String.format(sqlFormat, tableMetadata.getSchemaName(), tableMetadata.getPrimaryKeyName(), columnsAndParams,
                tableMetadata.getPrimaryKeyName(), paramSuffix, tableMetadata.getPrimaryKeyName());
    }

    public String createQueryWithParametersForInsert(String paramSuffix, MObject mObject, TableMetadata tableMetadata) {

        String sqlFormat = "INSERT INTO %s.%s (%s) VALUES (%s)";
        Boolean firstParam = true;
        String columnNames = " ";
        String paramNames = " ";

        for(Property p : mObject.getProperties()) {
            if(firstParam) {
                firstParam = false;
            } else {
                columnNames += ", ";
                paramNames += ", ";
            }

            columnNames += p.getDeveloperName();
            paramNames += ":" + paramSuffix + p.getDeveloperName();
        }

        return String.format(sqlFormat,tableMetadata.getSchemaName(), tableMetadata.getTableName(), columnNames, paramNames);
    }
}
