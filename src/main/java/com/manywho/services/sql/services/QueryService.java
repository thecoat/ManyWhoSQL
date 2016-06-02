package com.manywho.services.sql.services;

import com.healthmarketscience.sqlbuilder.*;
import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.sql.ServiceConfiguration;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.services.filter.QueryConditionsFromFilter;

import javax.inject.Inject;

public class QueryService {
    private QueryConditionsFromFilter queryConditionsFromFilter;

    @Inject
    public QueryService(QueryConditionsFromFilter queryConditionsFromFilter) {
        this.queryConditionsFromFilter = queryConditionsFromFilter;
    }

    public String createQueryWithParametersForSelectByPrimaryKey(TableMetadata tableMetadata, String primaryKeyParamName) {

        SelectQuery selectQuery = new SelectQuery().addAllColumns()
                .addCustomFromTable(tableMetadata.getTableName());

        selectQuery.addCondition(BinaryCondition.equalTo(new CustomSql(tableMetadata.getPrimaryKeyName()), new CustomSql(":" + primaryKeyParamName)));

        return selectQuery.toString();
    }

    public String createQueryWithParametersForUpdate(MObject mObject, TableMetadata tableMetadata){

        UpdateQuery updateQuery = new UpdateQuery(tableMetadata.getTableName());

        for(Property p : mObject.getProperties()) {
            updateQuery.addCustomSetClause(new CustomSql(p.getDeveloperName()), new CustomSql(":" + p.getDeveloperName()));
        }

        updateQuery.addCondition(BinaryCondition.equalTo(new CustomSql(tableMetadata.getPrimaryKeyName()), new CustomSql(":" + tableMetadata.getPrimaryKeyName())));

        return updateQuery.toString();
    }

    public String createQueryWithParametersForInsert(MObject mObject, TableMetadata tableMetadata) {
        InsertQuery insertQuery = new InsertQuery(tableMetadata.getTableName());

        for(Property p : mObject.getProperties()) {
            insertQuery.addCustomColumn(new CustomSql(p.getDeveloperName()), new CustomSql(":" + p.getDeveloperName()));
        }

        return  insertQuery.toString();
    }

    public String getSqlFromFilter(ServiceConfiguration configuration, ObjectDataType objectDataType, ListFilter filter) {

        SelectQuery selectQuery = new SelectQuery().addAllColumns()
                .addCustomFromTable(objectDataType.getDeveloperName());

        queryConditionsFromFilter.addSearch(selectQuery, filter.getSearch(), objectDataType.getProperties());
        queryConditionsFromFilter.addWhere(selectQuery, filter.getWhere());
        queryConditionsFromFilter.addOrderBy(selectQuery, filter.getOrderByPropertyDeveloperName(), filter.getOrderByDirectionType());
        queryConditionsFromFilter.addOffset(selectQuery, configuration.getDatabaseType(), filter.getOffset(), filter.getLimit());

        return selectQuery.validate().toString();
    }
}
