package com.zyq.frechwind.base;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class SqlUtil {

    public static String toSqlDate(Date date) {
        return "STR_TO_DATE('" + DateFormatUtils.format(date, "yyyy-MM-dd") + "', '%Y-%m-%d')";
    }

    public static String toSqlDate(String columnName, Date startDate) {
        Date endDate = DateUtils.addDays(startDate, 1);
        String sqlStartDate = " and " + columnName + " >= " + toSqlDate(startDate);
        String sqlEndDate = " and " + columnName + " < " + toSqlDate(endDate);

        return sqlStartDate + sqlEndDate;
    }

    public static String toSqlDate(String columnName, Date startDate, Date endDate) {
        String sqlStartDate = "";
        if (startDate != null) {
            sqlStartDate = " and " + columnName + " >= " + toSqlDate(startDate);
        }

        String sqlEndDate = "";
        if (endDate != null) {
            endDate = DateUtils.addDays(endDate, 1);
            sqlEndDate = " and " + columnName + " < " + toSqlDate(endDate);
        }

        return sqlStartDate + sqlEndDate;
    }

    public static String hourDiff(String timeColumn1, Date timeColumn2) {
        return "TIMESTAMPDIFF(HOUR, " + timeColumn1 + ", " + timeColumn2 + "))";
    }

    //删除掉一个where条件
    //这里简单处理
    public static String removeWhereClause(String sql, String columnName) {
        sql = sql.toLowerCase();
        Pattern p = Pattern.compile("\\s*(and)?\\s*" + columnName + "\\s*=\\s*'[^']+'");
        Matcher m = p.matcher(sql);
        if (m.find()) {
            String cc = m.group(0);
            sql = sql.replace(cc, "");
        }

        //如果去掉第一个条件之后，后面的会出现and开头的sql，因此需要去掉and
        sql = StringUtils.trim(sql);
        if (sql.startsWith("and ")) {
            sql = sql.substring(4, sql.length());
        }
        return sql;
    }

    public static void main(String[] args) {
        String sql = "del_flag='N' AND factory_id = '402880e6704172d30170419a71d10001'  AND isp_order_status = 'UnPay'  AND isp_order_status = 'UnPay' ";
        sql = removeWhereClause(sql, "isp_order_status");

        System.out.println(sql);
    }
}
