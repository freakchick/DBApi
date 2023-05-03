package com.gitee.freakchicken.dbapi.basic.executor;

import java.util.Map;

import org.apache.commons.text.StringSubstitutor;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.basic.domain.DataSource;
import com.gitee.freakchicken.dbapi.basic.dto.ESTaskDto;
import com.gitee.freakchicken.dbapi.basic.es.ESPoolManager;
import com.gitee.freakchicken.dbapi.basic.service.DataSourceService;
import com.gitee.freakchicken.dbapi.common.ApiConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ESExecutor {

    @Autowired
    DataSourceService dataSourceService;

    public Object execute(JSONObject taskJson, Map<String, Object> param) throws Exception {



        ESTaskDto task = taskJson.toJavaObject( ESTaskDto.class);

        DataSource datasource = dataSourceService.detail(task.getDatasourceId());
        if (datasource == null) {
            throw new RuntimeException("Datasource not exists!");
        }

        RestClient restClient = ESPoolManager.getPooledConnection(datasource);
        Request request = new Request(task.getMethod(), task.getEndpoint());

        StringSubstitutor StringSubstitutor = new StringSubstitutor(param);
        String content = StringSubstitutor.replace(task.getBody());
        request.setJsonEntity(content);

        Response response = restClient.performRequest(request);
        HttpEntity entity = response.getEntity();
        String s = EntityUtils.toString(entity);
        JSONObject parseObject = JSON.parseObject(s);

        return parseObject;

    }

}
