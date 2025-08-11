package com.jq.dbapi.util;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


public class SqlParser {
    private static final Logger logger = LoggerFactory.getLogger(SqlParser.class);


    public static void isSelect(String sql) {
        String dbType = JdbcConstants.MYSQL;

        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        SQLStatement stmt = stmtList.get(0);

        try {
            SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) stmt;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void getLimit(String sql) {
        String dbType = JdbcConstants.MYSQL;

        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        SQLStatement stmt = stmtList.get(0);


        SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) stmt;
        SQLSelectQuery sqlSelectQuery = sqlSelectStatement.getSelect().getQuery();

        SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) sqlSelectQuery;
        SQLLimit limit = sqlSelectQueryBlock.getLimit();
        System.out.println(limit.getRowCount());
    }

    public static List<String> getResponseParam(String sql) {
        String dbType = JdbcConstants.MYSQL;

        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        SQLStatement stmt = stmtList.get(0);

        List<SQLSelectItem> list = ((MySqlSelectQueryBlock) ((SQLSelect) ((SQLSelectStatement) stmt).getSelect()).getQuery()).getSelectList();

        List<String> collect = list.stream().map(t -> t.computeAlias()).collect(Collectors.toList());

        return collect;
    }

    public static List<String> getRequestParam(String sql) {
        String dbType = JdbcConstants.MYSQL;

        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        SQLStatement stmt = stmtList.get(0);

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        stmt.accept(visitor);

        Collection<TableStat.Column> columns = visitor.getColumns();
        List<String> list = columns.stream().filter(t -> {

            return t.isWhere() && t.getName().startsWith("$");
        })
                .map(t -> t.getName().substring(1)).collect(Collectors.toList());

        return list;
    }

    // 解析sql的表名
    public static List<String> getTableNameBySql(String sql) {
        String dbType = JdbcConstants.MYSQL;
//        switch (type) {
//            case "MYSQL":
//                dbType = JdbcConstants.MYSQL;
//                break;
//            case "POSTGRESQL":
//                dbType = JdbcConstants.POSTGRESQL;
//                break;
//            case "HIVE":
//                dbType = JdbcConstants.HIVE;
//                break;
//            default:
//        }
        try {
            List<String> tableNameList = new ArrayList<>();
            //格式化输出
            String sqlResult = SQLUtils.format(sql, dbType);
            logger.info("格式化后的sql:[{}]", sqlResult);

            List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
            if (CollectionUtils.isEmpty(stmtList)) {
                logger.info("stmtList为空无需获取");
                return Collections.emptyList();
            }
            for (SQLStatement sqlStatement : stmtList) {
                MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
                sqlStatement.accept(visitor);
                Map<TableStat.Name, TableStat> tables = visitor.getTables();
                logger.info("druid解析sql的结果集:[{}]", tables);
                Set<TableStat.Name> tableNameSet = tables.keySet();
                for (TableStat.Name name : tableNameSet) {
                    String tableName = name.getName();
                    if (StringUtils.isNotBlank(tableName)) {
                        tableNameList.add(tableName);
                    }
                }
            }
            logger.info("解析sql后的表名:[{}]", tableNameList);
            return tableNameList;
        } catch (Exception e) {
            logger.error("**************异常SQL:[{}]*****************\\n", sql);
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}

