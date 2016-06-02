package com.manywho.services.sql.services;

import com.healthmarketscience.sqlbuilder.*;
import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.services.filter.QueryFromFilter;

import javax.inject.Inject;

public class QueryService {
    private QueryFromFilter queryFromFilter;

    @Inject
    public QueryService(QueryFromFilter queryFromFilter) {
        this.queryFromFilter = queryFromFilter;
    }

    public String createQueryWithParametersForSelectByPrimaryKey(TableMetadata tableMetadata, String primaryKeyParamName) {

        SelectQuery selectQuery = new SelectQuery().addAllColumns()
                .addCustomFromTable(tableMetadata.getTableName());

        selectQuery.addCondition(BinaryCondition.equalTo(new CustomSql(tableMetadata.getPrimaryKeyName()), new CustomSql(":" + primaryKeyParamName)));

        return selectQuery.toString();
    }

    public String createQueryWithParametersForUpdate(String paramSuffix, MObject mObject, TableMetadata tableMetadata){

        UpdateQuery updateQuery = new UpdateQuery(tableMetadata.getTableName());

        for(Property p : mObject.getProperties()) {
            updateQuery.addCustomSetClause(new CustomSql(p.getDeveloperName()), new CustomSql(":" + paramSuffix + p.getDeveloperName()));
        }

        String primaryKeyParamName =  paramSuffix + tableMetadata.getPrimaryKeyName();
        updateQuery.addCondition(BinaryCondition.equalTo(new CustomSql(tableMetadata.getPrimaryKeyName()), new CustomSql(":" + primaryKeyParamName)));

        return updateQuery.toString();
    }

    public String createQueryWithParametersForInsert(String paramSuffix, MObject mObject, TableMetadata tableMetadata) {
        InsertQuery insertQuery = new InsertQuery(tableMetadata.getTableName());

        for(Property p : mObject.getProperties()) {
            insertQuery.addCustomColumn(new CustomSql(p.getDeveloperName()), new CustomSql(":" + paramSuffix + p.getDeveloperName()));
        }

        return  insertQuery.toString();
    }

    public String getSqlFromFilter(ServiceConfiguration configuration, ObjectDataType objectDataType, ListFilter filter) {

        SelectQuery selectQuery = new SelectQuery().addAllColumns()
                .addCustomFromTable(objectDataType.getDeveloperName());

        queryFromFilter.addSearch(selectQuery, filter.getSearch(), objectDataType.getProperties());
        queryFromFilter.addWhere(selectQuery, filter.getWhere());
        queryFromFilter.addOrderBy(selectQuery, filter.getOrderByPropertyDeveloperName(), filter.getOrderByDirectionType());
        queryFromFilter.addOffset(selectQuery, configuration.getDatabaseType(), filter.getOffset(), filter.getLimit());

        return selectQuery.validate().toString();
    }
}
