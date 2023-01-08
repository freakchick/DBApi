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

    public Object execute(ApiConfig config, DataSource datasource, Map<String, Object> param) throws Exception {

        ESApiConfig eSApiConfig = ESApiConfigService.getConfigByApiId(config.getId());

        RestClient restClient = ESPoolManager.getPooledConnection(datasource);
        Request request = new Request(eSApiConfig.getMethod(), eSApiConfig.getEndpoint());

        StringSubstitutor StringSubstitutor = new StringSubstitutor(param);
        String content = StringSubstitutor.replace(eSApiConfig.getBody());
        request.setJsonEntity(content);

        Response response = restClient.performRequest(request);
        HttpEntity entity = response.getEntity();
        String s = EntityUtils.toString(entity);
        JSONObject parseObject = JSON.parseObject(s);

        return parseObject;

    }

}
