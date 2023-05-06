package com.gitee.freakchicken.dbapi.basic.executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.basic.domain.DataSource;
import com.gitee.freakchicken.dbapi.basic.dto.ApiSqlDto;
import com.gitee.freakchicken.dbapi.basic.dto.SQLTaskDto;
import com.gitee.freakchicken.dbapi.basic.service.DataSourceService;
import com.gitee.freakchicken.dbapi.basic.util.JdbcUtil;
import com.gitee.freakchicken.dbapi.basic.util.PoolManager;
import com.gitee.freakchicken.dbapi.basic.util.SqlEngineUtil;
import com.gitee.freakchicken.dbapi.plugin.PluginManager;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;
import com.github.freakchick.orange.SqlMeta;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SQLExecutor implements Executor {

    @Autowired
    DataSourceService dataSourceService;

    @Override
    public Object execute(JSONObject taskJson, Map<String, Object> sqlParam) throws Exception {

        SQLTaskDto task = taskJson.toJavaObject(SQLTaskDto.class);

        DataSource datasource = dataSourceService.detail(task.getDatasourceId());
        if (datasource == null) {
            throw new RuntimeException("Datasource not exists!");
        }
        List<ApiSqlDto> sqlList = task.getSqlList();
        DruidPooledConnection connection = PoolManager.getPooledConnection(datasource);
        // 执行sql
        List<Object> dataList = executeSql(connection, sqlList, sqlParam, task.getTransaction());

        // 执行数据转换
        for (int i = 0; i < sqlList.size(); i++) {
            ApiSqlDto apiSql = sqlList.get(i);
            Object data = dataList.get(i);
            // 如果此单条sql是查询类sql，并且配置了数据转换插件
            if (data instanceof Iterable && StringUtils.isNotBlank(apiSql.getTransformPlugin())) {
                log.info("transform plugin execute");
                List<JSONObject> sourceData = (List<JSONObject>) (data); // 查询类sql的返回结果才可以这样强制转换，只有查询类sql才可以配置转换插件
                TransformPlugin transformPlugin = PluginManager.getTransformPlugin(apiSql.getTransformPlugin());
                Object resData = transformPlugin.transform(sourceData, apiSql.getTransformPluginParam());
                dataList.set(i, resData);// 重新设置值
            }
        }
        // 如果只有单条sql,返回结果不是数组格式
        return dataList.size() == 1 ? dataList.get(0) : dataList;
    }

    public List<Object> executeSql(Connection connection, List<ApiSqlDto> sqlList, Map<String, Object> sqlParam,
            boolean flag) {
        List<Object> dataList = new ArrayList<>();
        try {
            if (flag)
                connection.setAutoCommit(false);
            else
                connection.setAutoCommit(true);
            for (ApiSqlDto apiSql : sqlList) {
                SqlMeta sqlMeta = SqlEngineUtil.getEngine().parse(apiSql.getSqlText(), sqlParam);
                Object data = JdbcUtil.executeSql(connection, sqlMeta.getSql(), sqlMeta.getJdbcParamValues());
                dataList.add(data);
            }
            if (flag)
                connection.commit();
            return dataList;
        } catch (Exception e) {
            try {
                if (flag)
                    connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
