package com.manywho.services.sql.services.filter;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.CustomSql;
import com.healthmarketscience.sqlbuilder.OrderObject;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.custom.mysql.MysLimitClause;
import com.healthmarketscience.sqlbuilder.custom.postgresql.PgLimitClause;
import com.healthmarketscience.sqlbuilder.custom.postgresql.PgOffsetClause;
import com.healthmarketscience.sqlbuilder.custom.sqlserver.MssTopClause;
import com.manywho.sdk.api.run.elements.type.ListFilterWhere;
import com.manywho.sdk.api.run.elements.type.ObjectDataTypeProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class QueryFromFilter {
    public void addSearch(SelectQuery selectQuery, String search, List<ObjectDataTypeProperty> listProperties ) {
        if (StringUtils.isNotBlank(search)) {
            String searchTerm = "%" + search + "%";
            for(ObjectDataTypeProperty property: listProperties) {
                selectQuery.addCondition(BinaryCondition.like(new CustomSql(property.getDeveloperName()), searchTerm));

            }
        }
    }

    public void addWhere(SelectQuery selectQuery, List <ListFilterWhere> whereList) {
        for (ListFilterWhere filterWhere: whereList) {
            addAndCondition(selectQuery, filterWhere);
        }
    }

    private void addAndCondition(SelectQuery selectQuery, ListFilterWhere filterWhere) {
        switch (filterWhere.getCriteriaType()) {
            case Equal:
                selectQuery.addCondition(BinaryCondition.equalTo(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue()));
                break;
            case NotEqual:
                selectQuery.addCondition(BinaryCondition.notEqualTo(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue()));
                break;
            case GreaterThan:
                selectQuery.addCondition(BinaryCondition.greaterThan(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue(), false));
                break;
            case GreaterThanOrEqual:
                selectQuery.addCondition(BinaryCondition.greaterThan(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue(), true));
                break;
            case LessThan:
                selectQuery.addCondition(BinaryCondition.lessThan(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue(), false));
                break;
            case LessThanOrEqual:
                selectQuery.addCondition(BinaryCondition.lessThan(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue(), true));
                break;
            case Contains:
                selectQuery.addCondition(BinaryCondition.like(new CustomSql(filterWhere.getColumnName()), "%" + filterWhere.getContentValue() + "%"));
                break;
            case StartsWith:
                selectQuery.addCondition(BinaryCondition.like(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue() + "%"));
                break;
            case EndsWith:
                selectQuery.addCondition(BinaryCondition.like(new CustomSql(filterWhere.getColumnName()), "%" + filterWhere.getContentValue()));
                break;
            case IsEmpty:
                selectQuery.addCondition(BinaryCondition.equalTo(new CustomSql(filterWhere.getColumnName()), BinaryCondition.EMPTY));
                break;
            default:
                break;
        }
    }

    public void addOffset(SelectQuery selectQuery, String databaseType, Integer offset, Integer limit) {
        switch (databaseType) {
            case "postgresql":
                if(offset > 0) {
                    selectQuery.addCustomization(new PgOffsetClause(offset));
                }

                if( limit > 0) {
                    selectQuery.addCustomization(new PgLimitClause(limit));
                }
                break;
            case "mysql":
                if(offset > 0 || limit > 0) {
                    selectQuery.addCustomization(new MysLimitClause(offset, limit));
                }

                break;
            case "sqlserver":
                if(offset > 0) {
                    selectQuery.setOffset(offset);
                }

                if( limit > 0) {
                    selectQuery.addCustomization(new MssTopClause(limit));
                }
                break;
            // add oracle or any other jdbc supported database
            default:
                throw new RuntimeException("database type not supported");
        }
    }

    public void addOrderBy(SelectQuery selectQuery, String propertyName, String direction) {

        if (StringUtils.isBlank(propertyName)) {
            return;
        }

        switch(direction) {
            case "DESC":
                selectQuery.addCustomOrdering(new CustomSql(propertyName), OrderObject.Dir.DESCENDING);
                break;
            default:
                selectQuery.addCustomOrdering(new CustomSql(propertyName), OrderObject.Dir.ASCENDING);
        }
    }
}
