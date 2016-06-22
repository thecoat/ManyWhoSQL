package com.manywho.services.sql.services.filter;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.custom.mysql.MysLimitClause;
import com.healthmarketscience.sqlbuilder.custom.postgresql.PgLimitClause;
import com.healthmarketscience.sqlbuilder.custom.postgresql.PgOffsetClause;
import com.manywho.sdk.api.run.elements.type.ListFilterWhere;
import com.manywho.sdk.api.run.elements.type.ObjectDataTypeProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QueryFilterConditions {
    public void addSearch(SelectQuery selectQuery, String search, List<ObjectDataTypeProperty> listProperties ) {
        if (StringUtils.isNotBlank(search)) {
            String searchTerm = "%" + search + "%";
            for(ObjectDataTypeProperty property: listProperties) {
                selectQuery.addCondition(BinaryCondition.like(new CustomSql(property.getDeveloperName()), searchTerm));
            }
        }
    }

    public void addWhere(SelectQuery selectQuery, List <ListFilterWhere> whereList, String comparisonType) {
        ArrayList<Condition> conditions = new ArrayList<>();

        for (ListFilterWhere filterWhere: whereList) {
            conditions.add(getConditionFromFilterElement(filterWhere));
        }

        if (StringUtils.equals(comparisonType, "OR")) {
            selectQuery.addCondition(ComboCondition.or(conditions.toArray()));
        }else if (StringUtils.equals(comparisonType, "AND")) {
            selectQuery.addCondition(ComboCondition.and(conditions.toArray()));
        }
    }

    private BinaryCondition getConditionFromFilterElement(ListFilterWhere filterWhere) {

        switch (filterWhere.getCriteriaType()) {
            case Equal:
               return BinaryCondition.equalTo(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue());
            case NotEqual:
               return BinaryCondition.notEqualTo(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue());
            case GreaterThan:
               return BinaryCondition.greaterThan(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue(), false);
            case GreaterThanOrEqual:
               return BinaryCondition.greaterThan(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue(), true);
            case LessThan:
               return BinaryCondition.lessThan(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue(), false);
            case LessThanOrEqual:
               return BinaryCondition.lessThan(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue(), true);
            case Contains:
               return BinaryCondition.like(new CustomSql(filterWhere.getColumnName()), "%" + filterWhere.getContentValue() + "%");
            case StartsWith:
               return BinaryCondition.like(new CustomSql(filterWhere.getColumnName()), filterWhere.getContentValue() + "%");
            case EndsWith:
               return BinaryCondition.like(new CustomSql(filterWhere.getColumnName()), "%" + filterWhere.getContentValue());
            case IsEmpty:
               return BinaryCondition.equalTo(new CustomSql(filterWhere.getColumnName()), BinaryCondition.EMPTY);
            default:
                break;
        }
        return null;
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
                    //selectQuery.addCustomization(new MssTopClause(limit));
                    selectQuery.setFetchNext(limit);
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
