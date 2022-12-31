package com.gitee.freakchicken.dbapi.basic.executor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.basic.domain.DataSource;

import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.plugin.CachePlugin;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.client.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.http.HttpEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.http.util.EntityUtils;
import com.gitee.freakchicken.dbapi.basic.service.ESApiConfigService;
import com.gitee.freakchicken.dbapi.basic.domain.ESApiConfig;
import com.gitee.freakchicken.dbapi.basic.es.ESPoolManager;
import com.gitee.freakchicken.dbapi.basic.util.ThreadUtils;

import com.gitee.freakchicken.dbapi.plugin.PluginManager;

import com.gitee.freakchicken.dbapi.plugin.AlarmPlugin;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ESExecutor {

    @Autowired
    ESApiConfigService ESApiConfigService;

    public ResponseDto execute(ApiConfig config, DataSource datasource, Map<String, Object> param,
            HttpServletRequest servletRequest)
            throws Exception {

        ESApiConfig eSApiConfig = null;
        try {

            eSApiConfig = ESApiConfigService.getConfigByApiId(config.getId());
            // 从缓存获取数据
            if (StringUtils.isNoneBlank(eSApiConfig.getCachePlugin())) {
                CachePlugin cachePlugin = PluginManager.getCachePlugin(eSApiConfig.getCachePlugin());
                Object o = cachePlugin.get(config, param);
                if (o != null) {
                    return ResponseDto.apiSuccess(o); // 如果缓存有数据直接返回
                }
            }

            RestClient restClient = ESPoolManager.getPooledConnection(datasource);

            Request request = new Request(eSApiConfig.getMethod(), eSApiConfig.getEndpoint());

            StringSubstitutor StringSubstitutor = new StringSubstitutor(param);
            String content = StringSubstitutor.replace(eSApiConfig.getBody());
            request.setJsonEntity(content);

            Response response = restClient.performRequest(request);
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity);
            JSONObject parseObject = JSON.parseObject(s);

            return ResponseDto.apiSuccess(parseObject);
        } catch (Exception e) {
            if (StringUtils.isNotBlank(eSApiConfig.getAlarmPlugin())) {
                try {
                    String pluginParam = eSApiConfig.getAlarmPluginParam();
                    AlarmPlugin alarmPlugin = PluginManager.getAlarmPlugin(eSApiConfig.getAlarmPlugin());
                    ThreadUtils.submitAlarmTask(new Runnable() {
                        @Override
                        public void run() {
                            alarmPlugin.alarm(e, config, servletRequest, pluginParam);
                        }
                    });
                } catch (Exception error) {
                    log.error(eSApiConfig.getAlarmPlugin() + " error!", error);
                }
            }
            throw new RuntimeException(e.getMessage());
        }
    }

}
