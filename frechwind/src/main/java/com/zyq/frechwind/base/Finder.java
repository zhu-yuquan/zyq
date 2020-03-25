package com.zyq.frechwind.base;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Finder {
    private String entityName;
    private String entityAlias;
    private List<FinderColumn> colList = new ArrayList<FinderColumn>();
    private List<FinderColumn> orderList = new ArrayList<FinderColumn>();
    private Pager pager;

    public Finder(String entityName) {
        this.entityName = entityName;
    }

    public Finder(String entityName, Pager pager) {
        this.entityName = entityName;
        this.pager = pager;
    }

    public Finder equalNotNull(String columnName, Serializable value) {
        if (value == null) {
            throw new AppException(columnName + " can't is null.");
        }
        if (value instanceof String) {
            if (StringUtils.isBlank((String) value)) {
                throw new AppException(columnName + " can't is space.");
            }
        }
        return equal(columnName, value);
    }

    public Finder equal(String columnName, Serializable value) {
        if (value == null) {
            return this;
        }

        if (value instanceof Date) {
            throw new AppException("日期不可以使用equal参数，建议用equalDate或者hql进行查询");
        }

        FinderColumn finderColumn = new FinderColumn(FinderColumn.ColumnOperater.EQUAL, columnName);
        colList.add(finderColumn.addValue(value));

        return this;
    }

    public Finder equalDate(String columnName, Date date) {
        if (date == null) {
            return this;
        }

        date = DateUtils.truncate(date, Calendar.DATE);
        hql(columnName + " >= STR_TO_DATE('" + DateFormatUtils.format(date, "yyyy-MM-dd") + "', '%Y-%m-%d')");

        date = DateUtils.addDays(date, 1);
        hql(columnName + " < STR_TO_DATE('" + DateFormatUtils.format(date, "yyyy-MM-dd") + "', '%Y-%m-%d')");
        return this;
    }

    public Finder isNull(String columnName) {
        FinderColumn finderColumn = new FinderColumn(FinderColumn.ColumnOperater.IS_NULL, columnName);
        colList.add(finderColumn.addValue(" is null "));
        return this;
    }

    public Finder notEqual(String columnName, Serializable value) {
        FinderColumn finderColumn = new FinderColumn(FinderColumn.ColumnOperater.STRING_UN_EQUAL, columnName);
        colList.add(finderColumn.addValue(value));
        return this;
    }

    public Finder like(String columnName, String value) {
        FinderColumn finderColumn = new FinderColumn(FinderColumn.ColumnOperater.STRING_LIKE, columnName);
        colList.add(finderColumn.addValue(value));
        return this;
    }

    public Finder in(String columnName, String... values) {
        FinderColumn finderColumn = new FinderColumn(FinderColumn.ColumnOperater.IN, columnName);
        for (String o : values) {
            finderColumn.addValue(o);
        }
        colList.add(finderColumn);
        return this;
    }

    public Finder greaterThan(String columnName, String value) {
        FinderColumn finderColumn = new FinderColumn(FinderColumn.ColumnOperater.GREATER_THAN, columnName);
        colList.add(finderColumn.addValue(value));
        return this;
    }

    public Finder greaterEqualThan(String columnName, String value) {
        FinderColumn finderColumn = new FinderColumn(FinderColumn.ColumnOperater.GREATER_EQ_THAN, columnName);
        colList.add(finderColumn.addValue(value));
        return this;
    }

    public Finder greaterEqualThan(String columnName, Date value) {
        FinderColumn finderColumn = new FinderColumn(FinderColumn.ColumnOperater.GREATER_EQ_THAN, columnName);
        colList.add(finderColumn.addValue(value));
        return this;
    }

    public Finder lessThan(String columnName, String value) {
        FinderColumn finderColumn = new FinderColumn(FinderColumn.ColumnOperater.LESS_THAN, columnName);
        colList.add(finderColumn.addValue(value));
        return this;
    }

    public Finder lessEqualThan(String columnName, String value) {
        FinderColumn finderColumn = new FinderColumn(FinderColumn.ColumnOperater.LESS_EQ_THAN, columnName);
        colList.add(finderColumn.addValue(value));
        return this;
    }

    public Finder lessEqualThan(String columnName, Date value) {
        FinderColumn finderColumn = new FinderColumn(FinderColumn.ColumnOperater.LESS_EQ_THAN, columnName);
        colList.add(finderColumn.addValue(value));
        return this;
    }

    public Finder hql(String hql) {
        if (StringUtils.isNotBlank(hql)) {
            FinderColumn finderColumn = new FinderColumn(FinderColumn.ColumnOperater.HQL, hql);
            colList.add(finderColumn.addValue(hql));
        }
        return this;
    }

    /**
     * @param columnName
     * @param descOrAsc  desc或者asc
     * @return
     */
    public Finder order(String columnName, String descOrAsc) {
        FinderColumn finderColumn = new FinderColumn(FinderColumn.ColumnOperater.EQUAL, columnName);
        finderColumn.addValue(descOrAsc);
        orderList.add(finderColumn);
        return this;
    }

    @SuppressWarnings("unchecked")
    public KeyValue<String, Map<String, Serializable>> toHql() {
        Map<String, Serializable> valueMap = new HashMap<String, Serializable>();
        StringBuilder hql = new StringBuilder();
        StringBuilder whereClause = new StringBuilder();
        StringBuilder orderClause = new StringBuilder();
        hql.append("from " + entityName);
        if (StringUtils.isNotBlank(entityAlias)) {
            hql.append(" " + entityAlias);
        }

        for (int i = 0; i < colList.size(); i++) {
            FinderColumn col = colList.get(i);
            if (col.getValues().size() == 0) {
                continue;
            }
            String valueName = col.getColumnName().replaceAll("\\.", "_");

            if (whereClause.length() > 0) {
                whereClause.append(" and ");
            }

            //以下部分对value进行处理，例如like，改为%value%、in的话，两端加上单引号
            Serializable value = null;
            switch (col.getColumnOperater()) {
                case STRING_LIKE:
                    value = "%" + col.getValue(0) + "%";
                    break;
                case IN:
                    StringBuilder buf = new StringBuilder();
                    for (Serializable s : col.getValues()) {
                        if (buf.length() > 0) {
                            buf.append(",");
                        }
                        buf.append("'" + s + "'");
                    }
                    value = buf.toString();
                    break;
                case IS_NULL:
                    value = "";
                    break;
                default:
                    value = col.getValue(0);
            }

            if (!FinderColumn.ColumnOperater.HQL.equals(col.getColumnOperater()) && !FinderColumn.ColumnOperater.IS_NULL.equals(col.getColumnOperater())) {
                valueMap.put(valueName + "_" + i, value);
            }

            if (FinderColumn.ColumnOperater.IS_NULL.equals(col.getColumnOperater())) {
                whereClause.append(col.getColumnName() + " " + col.getColumnOperater().getCode());
            } else if (FinderColumn.ColumnOperater.HQL.equals(col.getColumnOperater())) {
                whereClause.append("(" + col.getColumnName() + ")");
            } else if (FinderColumn.ColumnOperater.IN.equals(col.getColumnOperater())) {
                whereClause.append(col.getColumnName() + " in (" + value + ")");
            } else {
                whereClause.append(col.getColumnName() + " " + col.getColumnOperater().getCode() + " :" + valueName + "_" + i);
            }
        }

        //生成 order by 子句
        for (FinderColumn col : orderList) {
            if (orderClause.length() > 0) {
                orderClause.append(", ");
            }
            orderClause.append(col.getColumnName() + " " + col.getValue());
        }

        //添加where关键字
        if (valueMap.size() > 0) {
            hql.append(" where ");
            hql.append(whereClause);
        }

        //添加order by关键字
        if (orderClause.length() > 0) {
            hql.append(" order by ");
            hql.append(orderClause);
        }

        return new KeyValue<String, Map<String, Serializable>>(hql.toString(), valueMap);
    }

    public static String[] listFieldArray(List<? extends Object> list, String fieldName) {
        List<String> olist = new ArrayList<String>();
        for (Object o : list) {
            String v = null;
            try {
                v = BeanUtils.getProperty(o, fieldName).toString();
                olist.add(v);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return olist.toArray(new String[0]);
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public Map<String, Object> getParaMap() {
        Map map = new HashMap();
        for (FinderColumn fc : colList) {
            map.put(fc.getColumnName(), fc.getValue());
        }
        return map;
    }

    public String getEntityAlias() {
        return entityAlias;
    }

    public void setEntityAlias(String entityAlias) {
        this.entityAlias = entityAlias;
    }
}