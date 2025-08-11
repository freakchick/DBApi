package com.jq.dbapi.service;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jq.dbapi.domain.ApiConfig;
import com.jq.dbapi.domain.DataSource;
import com.jq.dbapi.util.PoolManager;
import com.jq.dbapi.util.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class ApiService {

    public String buildSql(HttpServletRequest request, ApiConfig config) {
        String sql = config.getSql();

        JSONArray requestParams = JSON.parseArray(config.getParams());
        for (int i = 0; i < requestParams.size(); i++) {
            JSONObject jo = requestParams.getJSONObject(i);
            String name = jo.getString("name");
            String type = jo.getString("type");
            String old = '$' + name;

            String value = request.getParameter(name);

            //不是数字类型的值要加单引号
            if (!"number".equals(type)) {
                value = String.format("'%s'", value);
            }

            sql = sql.replace(old, value);
        }

        return sql;
    }

    public ResponseDto executeSql(String sql, DataSource datasource) {
        DruidPooledConnection connection = null;
        try {

            connection = PoolManager.getPooledConnection(datasource);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();

            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rs.getMetaData().getColumnLabel(i);
                columns.add(columnName);
            }
            List<JSONObject> list = new ArrayList<>();
            while (rs.next()) {
                JSONObject jo = new JSONObject();
                columns.stream().forEach(t -> {
                    try {
                        Object value = rs.getObject(t);
                        jo.put(t, value);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
                list.add(jo);
            }
            return ResponseDto.apiSuccess(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.fail(e.getMessage());
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
