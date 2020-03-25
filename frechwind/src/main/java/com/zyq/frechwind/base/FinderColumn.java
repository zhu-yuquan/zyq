package com.zyq.frechwind.base;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class FinderColumn {
    private String columnName;
    private ArrayList<Serializable> values = new ArrayList<Serializable>();
    private ColumnOperater columnOperater;

    public enum ColumnOperater {
        EQUAL("="),
        STRING_LIKE("LIKE"),
        IS_NULL(" is null"),
        STRING_UN_EQUAL("!="),
        IN("in"),
        GREATER_THAN(">"),
        GREATER_EQ_THAN(">="),
        LESS_THAN("<"),
        LESS_EQ_THAN("<="),
        HQL("HQL");

        private String code;

        private ColumnOperater(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public FinderColumn(ColumnOperater columnOperater, String columnName) {
        this.columnOperater = columnOperater;
        this.columnName = columnName;
    }

    public FinderColumn addValue(Serializable value) {
        if (value != null) {
            if (value instanceof String) {
                String valueStr = (String) value;
                String[] valueArray = StringUtils.split(valueStr, ",");
                for (String val : valueArray) {
                    values.add(val);
                }
            } else {
                values.add(value);
            }
        }
        return this;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Object getValue() {
        if (values.size() > 0) {
            return values.get(0);
        } else {
            return null;
        }
    }

    public ArrayList<Serializable> getValues() {
        return values;
    }

    public int valueSize() {
        return values.size();
    }

    public Serializable getValue(int i) {
        return values.get(i);
    }

    public void setValues(ArrayList<Serializable> values) {
        this.values = values;
    }

    public ColumnOperater getColumnOperater() {
        return columnOperater;
    }

    public void setColumnOperater(ColumnOperater columnOperater) {
        this.columnOperater = columnOperater;
    }

    public String toSql() {
        return "";
    }
}
