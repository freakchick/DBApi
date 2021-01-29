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

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-20 15:36
 **/
@Service
@Slf4j
public class ApiService {

    public List<Object> getSqlParam(HttpServletRequest request, ApiConfig config) {
        List<Object> list = new ArrayList<>();

        JSONArray requestParams = JSON.parseArray(config.getParams());
        for (int i = 0; i < requestParams.size(); i++) {
            JSONObject jo = requestParams.getJSONObject(i);
            String name = jo.getString("name");
            String type = jo.getString("type");

            String value = request.getParameter(name);

            switch (type) {
                case "double":
                    Double v = Double.valueOf(value);
                    list.add(v);
                    break;
                case "bigint":
                    Long longV = Long.valueOf(value);
                    list.add(longV);
                    break;
                case "string":
                case "date":
                    list.add(value);
                    break;
            }
        }
        return list;
    }

    public ResponseDto executeSql(ApiConfig apiConfig, DataSource datasource, List<Object> sqlParam) {
        String sql = apiConfig.getRealSql();
        DruidPooledConnection connection = null;
        try {

            connection = PoolManager.getPooledConnection(datasource);
            PreparedStatement statement = connection.prepareStatement(sql);

            //参数注入
            for (int i = 1; i <= sqlParam.size(); i++) {
                statement.setObject(i, sqlParam.get(i - 1));
            }

            if (apiConfig.getIsSelect() == 1) {
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
