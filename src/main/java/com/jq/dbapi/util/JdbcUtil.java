package com.jq.dbapi.util;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.fastjson.JSONObject;
import com.jq.dbapi.domain.DataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JdbcUtil {

    public static ResultSet query(String sql, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet;
    }

    public static Connection getConnection(DataSource ds) throws SQLException, ClassNotFoundException {
        String url = ds.getUrl();
        switch (ds.getType()) {
            case JdbcConstants.MYSQL:
                Class.forName("com.mysql.jdbc.Driver");
                break;
            case JdbcConstants.POSTGRESQL:
                Class.forName("org.postgresql.Driver");
                break;
            case JdbcConstants.HIVE:
                Class.forName("org.apache.hive.jdbc.HiveDriver");
                break;
            case JdbcConstants.SQL_SERVER:
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                break;
            default:
                break;
        }

        Connection connection = DriverManager.getConnection(url, ds.getUsername(), ds.getPassword());
        log.info("获取连接成功");
        return connection;
    }

    /**
     * 查询库中所有表
     * @param conn
     * @param type
     * @return
     */
    public static List<String> getAllTables(Connection conn, String type) {
        List<String> list = new ArrayList<>();
        PreparedStatement pst = null;
        try {
            String sql;
            switch (type) {
                case "MYSQL":
                case "HIVE":
                    sql = "show tables";
                    break;
                case "POSTGRESQL":
                    sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' ORDER BY table_name";
                    break;
                // TODO
                case "SQLSERVER":
                    sql = "select * from sys.tables";
                    break;
                default:
                    sql = "show tables";

            }

            pst = conn.prepareStatement(sql);
            ResultSet resultSet = pst.executeQuery();

            while (resultSet.next()) {
                String s = resultSet.getString(1);
                list.add(s);
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            try {
                if (pst != null)
                    pst.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询表所有字段
     * @param conn
     * @param type
     * @param table
     * @return
     */
    public static List<JSONObject> getRDBMSColumnProperties(Connection conn, String type, String table) {
        List<JSONObject> list = new ArrayList<>();
        PreparedStatement pst = null;
        try {
            String sql;
            switch (type) {
                case "POSTGRESQL":
                    sql = "select * from \"" + table + "\" where 1=2";
                    break;
                default:
                    sql = "select * from " + table + " where 1=2";
            }
            log.info(sql);
            pst = conn.prepareStatement(sql);
            ResultSetMetaData rsd = pst.executeQuery().getMetaData();

            for (int i = 0; i < rsd.getColumnCount(); i++) {
                JSONObject jsonObject = new JSONObject();

                String columnTypeName = rsd.getColumnTypeName(i + 1);
                jsonObject.put("fieldTypeName", columnTypeName);//数据库字段类型名
                jsonObject.put("TypeName",  columnTypeName);
                jsonObject.put("fieldJavaTypeName", rsd.getColumnClassName(i + 1));//映射到java的类型名
                String columnName = rsd.getColumnName(i + 1);
                if (columnName.contains("."))
                    columnName = columnName.split("\\.")[1];
                jsonObject.put("fieldName", columnName);//表字段
                list.add(jsonObject);
            }
            return list;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            try {
                if (pst != null)
                    pst.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}