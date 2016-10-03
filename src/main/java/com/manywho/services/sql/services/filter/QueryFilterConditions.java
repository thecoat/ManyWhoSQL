package com.manywho.services.sql.services.filter;

import com.google.common.base.Strings;
import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.custom.mysql.MysLimitClause;
import com.healthmarketscience.sqlbuilder.custom.postgresql.PgLimitClause;
import com.healthmarketscience.sqlbuilder.custom.postgresql.PgOffsetClause;
import com.manywho.sdk.api.ComparisonType;
import com.manywho.sdk.api.run.elements.type.ListFilterWhere;
import com.manywho.sdk.api.run.elements.type.ObjectDataTypeProperty;
import com.manywho.services.sql.entities.TableMetadata;
import com.manywho.services.sql.utilities.ScapeForTablesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QueryFilterConditions {
    public void addSearch(SelectQuery selectQuery, String search, List<ObjectDataTypeProperty> listProperties, String databaseType) {
        if (!Strings.isNullOrEmpty(search)) {
            String searchTerm = "%" + search + "%";
            for(ObjectDataTypeProperty property: listProperties) {
                selectQuery.addCondition(BinaryCondition.like(new CustomSql(ScapeForTablesUtil.scapeCollumnName(databaseType, property.getDeveloperName())), searchTerm));
            }
        }
    }

    public void addWhere(SelectQuery selectQuery, List <ListFilterWhere> whereList, ComparisonType comparisonType, String databaseType) {
        ArrayList<Condition> conditions = new ArrayList<>();
        if(whereList == null) return;

        String comparisonTypeLocal = "";

        for (ListFilterWhere filterWhere: whereList) {
            conditions.add(getConditionFromFilterElement(filterWhere, databaseType));
        }

        if(comparisonType!=null) {
            comparisonTypeLocal = comparisonType.toString();
        }

        if ("OR".equals(comparisonTypeLocal)) {
            selectQuery.addCondition(ComboCondition.or(conditions.toArray()));
        }else if ("AND".equals(comparisonTypeLocal)) {
            selectQuery.addCondition(ComboCondition.and(conditions.toArray()));
        }
    }

    private BinaryCondition getConditionFromFilterElement(ListFilterWhere filterWhere, String databaseType) {

        switch (filterWhere.getCriteriaType()) {
            case Equal:
               return BinaryCondition.equalTo(new CustomSql(ScapeForTablesUtil.scapeCollumnName(databaseType, filterWhere.getColumnName())), filterWhere.getContentValue());
            case NotEqual:
               return BinaryCondition.notEqualTo(new CustomSql(ScapeForTablesUtil.scapeCollumnName(databaseType, filterWhere.getColumnName())), filterWhere.getContentValue());
            case GreaterThan:
               return BinaryCondition.greaterThan(new CustomSql(ScapeForTablesUtil.scapeCollumnName(databaseType, filterWhere.getColumnName())), filterWhere.getContentValue(), false);
            case GreaterThanOrEqual:
               return BinaryCondition.greaterThan(new CustomSql(ScapeForTablesUtil.scapeCollumnName(databaseType, filterWhere.getColumnName())), filterWhere.getContentValue(), true);
            case LessThan:
               return BinaryCondition.lessThan(new CustomSql(ScapeForTablesUtil.scapeCollumnName(databaseType, filterWhere.getColumnName())), filterWhere.getContentValue(), false);
            case LessThanOrEqual:
               return BinaryCondition.lessThan(new CustomSql(ScapeForTablesUtil.scapeCollumnName(databaseType, filterWhere.getColumnName())), filterWhere.getContentValue(), true);
            case Contains:
               return BinaryCondition.like(new CustomSql(ScapeForTablesUtil.scapeCollumnName(databaseType, filterWhere.getColumnName())), "%" + filterWhere.getContentValue() + "%");
            case StartsWith:
               return BinaryCondition.like(new CustomSql(ScapeForTablesUtil.scapeCollumnName(databaseType, filterWhere.getColumnName())), filterWhere.getContentValue() + "%");
            case EndsWith:
               return BinaryCondition.like(new CustomSql(ScapeForTablesUtil.scapeCollumnName(databaseType, filterWhere.getColumnName())), "%" + filterWhere.getContentValue());
            case IsEmpty:
               return BinaryCondition.equalTo(new CustomSql(ScapeForTablesUtil.scapeCollumnName(databaseType, filterWhere.getColumnName())), BinaryCondition.EMPTY);
            default:
                break;
        }
        return null;
    }

    public void addOffset(SelectQuery selectQuery, String databaseType, Integer offset, Integer limit) {

        if(limit <= 0 || limit > 1000) {
            limit = 1000;
        }
        switch (databaseType) {
            case "postgresql":
                    selectQuery.addCustomization(new PgOffsetClause(offset));
                    selectQuery.addCustomization(new PgLimitClause(limit));

                break;
            case "mysql":
                    selectQuery.addCustomization(new MysLimitClause(offset, limit));

                break;
            case "sqlserver":
                    selectQuery.setOffset(offset);
                    //selectQuery.addCustomization(new MssTopClause(limit));
                    selectQuery.setFetchNext(limit);

                break;
            // add oracle or any other jdbc supported database
            default:
                throw new RuntimeException("database type not supported");
        }
    }

    /**
     * It will order by orderByPropertyName, if this field is empty will order by the primary Keys
     *
     * @param selectQuery
     * @param orderByPropertyName
     * @param direction
     * @param tableMetadata
     * @param databaseType
     */
    public void addOrderBy(SelectQuery selectQuery, String orderByPropertyName, String direction, TableMetadata tableMetadata, String databaseType) {
        List<String> properties;

        if (Strings.isNullOrEmpty(orderByPropertyName)) {
            properties = tableMetadata.getPrimaryKeyNames();
        } else {
            properties = new ArrayList<>();
            properties.add(orderByPropertyName);
        }

        OrderObject.Dir typeDirection;

        if (Objects.equals(direction, "DESC")) {
            typeDirection = OrderObject.Dir.DESCENDING;
        } else {
            typeDirection = OrderObject.Dir.ASCENDING;
        }

        for (String property: properties) {
            selectQuery.addCustomOrdering(new CustomSql(ScapeForTablesUtil.scapeCollumnName(databaseType, property)), typeDirection);
        }
    }
}
