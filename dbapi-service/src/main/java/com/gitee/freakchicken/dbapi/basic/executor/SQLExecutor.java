package com.gitee.freakchicken.dbapi.basic.executor;

import com.alibaba.druid.pool.DruidPooledConnection;

import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.basic.domain.ApiSql;
import com.gitee.freakchicken.dbapi.basic.domain.DataSource;
import com.gitee.freakchicken.dbapi.basic.domain.SQLApiConfig;
import com.gitee.freakchicken.dbapi.basic.util.JdbcUtil;
import com.gitee.freakchicken.dbapi.basic.util.PoolManager;
import com.gitee.freakchicken.dbapi.basic.util.SqlEngineUtil;
import com.gitee.freakchicken.dbapi.basic.util.ThreadUtils;
import com.gitee.freakchicken.dbapi.basic.service.SQLApiConfigService;

import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.plugin.CachePlugin;
import com.gitee.freakchicken.dbapi.plugin.PluginManager;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;
import com.gitee.freakchicken.dbapi.plugin.AlarmPlugin;
import com.github.freakchick.orange.SqlMeta;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class SQLExecutor {

    @Autowired
    SQLApiConfigService SQLApiConfigService;

    public Object execute(ApiConfig config, DataSource datasource, Map<String, Object> sqlParam) throws Exception {
        SQLApiConfig SQLApiConfig  = SQLApiConfigService.getConfigByApiId(config.getId());

        List<ApiSql> sqlList = SQLApiConfig.getSqlList();

        DruidPooledConnection connection = PoolManager.getPooledConnection(datasource);
        boolean flag = SQLApiConfig.getOpenTrans() == 1 ? true : false;
        // 执行sql
        List<Object> dataList = executeSql(connection, sqlList, sqlParam, flag);

        // 执行数据转换
        for (int i = 0; i < sqlList.size(); i++) {
            ApiSql apiSql = sqlList.get(i);
            Object data = dataList.get(i);
            // 如果此单条sql是查询类sql，并且配置了数据转换插件
            if (data instanceof Iterable && StringUtils.isNotBlank(apiSql.getTransformPlugin())) {
                log.info("transform plugin execute");
                List<JSONObject> sourceData = (List<JSONObject>) (data); // 查询类sql的返回结果才可以这样强制转换，只有查询类sql才可以配置转换插件
                TransformPlugin transformPlugin = PluginManager.getTransformPlugin(apiSql.getTransformPlugin());
                Object resData = transformPlugin.transform(sourceData, apiSql.getTransformPluginParams());
                dataList.set(i, resData);// 重新设置值
            }
        }
        Object res = dataList;
        // 如果只有单条sql,返回结果不是数组格式
        if (dataList.size() == 1) {
            res = dataList.get(0);
        }

        return res;

    }

    public List<Object> executeSql(Connection connection, List<ApiSql> sqlList, Map<String, Object> sqlParam, boolean flag) {
        List<Object> dataList = new ArrayList<>();
        try {
            if (flag)
                connection.setAutoCommit(false);
            else
                connection.setAutoCommit(true);
            for (ApiSql apiSql : sqlList) {
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
