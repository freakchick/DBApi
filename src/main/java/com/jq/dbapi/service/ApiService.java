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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-20 15:36
 **/
@Service
@Slf4j
public class ApiService {

    public Map<String, Object> getSqlParam(HttpServletRequest request, ApiConfig config) {
        Map<String, Object> map = new HashMap<>();

        JSONArray requestParams = JSON.parseArray(config.getParams());
        for (int i = 0; i < requestParams.size(); i++) {
            JSONObject jo = requestParams.getJSONObject(i);
            String name = jo.getString("name");
            String type = jo.getString("type");

            //数组类型参数
            if (type.startsWith("list")) {
                String[] values = request.getParameterValues(name);
                List<String> list = Arrays.asList(values);
                if (values.length > 0) {
                    switch (type) {
                        case "list<double>":
                            List<Double> collect = list.stream().map(value -> Double.valueOf(value)).collect(Collectors.toList());
                            map.put(name, collect);
                            break;
                        case "list<bigint>":
                            List<Long> longs = list.stream().map(value -> Long.valueOf(value)).collect(Collectors.toList());
                            map.put(name, longs);
                            break;
                        case "list<string>":
                        case "list<date>":
                            map.put(name, list);
                            break;
                    }
                } else {
                    map.put(name, list);
                }
            } else {

                String value = request.getParameter(name);
                if (StringUtils.isNotBlank(value)) {

                    switch (type) {
                        case "double":
                            Double v = Double.valueOf(value);
                            map.put(name, v);
                            break;
                        case "bigint":
                            Long longV = Long.valueOf(value);
                            map.put(name, longV);
                            break;
                        case "string":
                        case "date":
                            map.put(name, value);
                            break;
                    }
                } else {
                    map.put(name, value);
                }
            }
        }
        return map;
    }

    public ResponseDto executeSql(int isSelect, DataSource datasource, String sql, List<Object> jdbcParamValues) {

        DruidPooledConnection connection = null;
        try {

            connection = PoolManager.getPooledConnection(datasource);
            PreparedStatement statement = connection.prepareStatement(sql);
            //参数注入
            for (int i = 1; i <= jdbcParamValues.size(); i++) {
                statement.setObject(i, jdbcParamValues.get(i - 1));
            }

            if (isSelect == 1) {
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
            } else {
                int rs = statement.executeUpdate();
                return ResponseDto.apiSuccess("sql修改数据行数： " + rs);
            }
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
