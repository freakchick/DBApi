package com.gitee.freakchicken.dbapi.basic.service;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.gitee.freakchicken.dbapi.basic.dao.ApiConfigMapper;
import com.gitee.freakchicken.dbapi.basic.dao.ApiPluginConfigMapper;
import com.gitee.freakchicken.dbapi.basic.dao.DataSourceMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiDto;
import com.gitee.freakchicken.dbapi.basic.util.Constants;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.common.ApiPluginConfig;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.plugin.CachePlugin;
import com.gitee.freakchicken.dbapi.plugin.PluginManager;

import lombok.extern.slf4j.Slf4j;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-19 17:27
 **/
@Slf4j
@Service
@DS("meta-db")
public class ApiConfigService {

    @Autowired
    ApiConfigMapper apiConfigMapper;
    @Autowired
    DataSourceMapper dataSourceMapper;

    @Autowired
    ApiPluginConfigMapper pluginConfigMapper;
    @Autowired
    CacheManager cacheManager;

    @Value("${dbapi.api.context}")
    String apiContext;

    @Transactional
    public ResponseDto add(ApiConfig apiConfig, List<ApiPluginConfig> list) {
        int size = apiConfigMapper.selectCountByPath(apiConfig.getPath());
        if (size > 0) {
            return ResponseDto.fail("Path has been used!");
        } else {

            if (MediaType.APPLICATION_JSON_VALUE.equals(apiConfig.getContentType())) {
                apiConfig.setParams("[]"); // 不能设置null 前端使用会报错
            } else if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(apiConfig.getContentType())) {
                apiConfig.setJsonParam(null);
            }

            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            apiConfig.setCreateTime(now);
            apiConfig.setUpdateTime(now);
            apiConfigMapper.insert(apiConfig);

            list.stream().forEach(t -> {
                pluginConfigMapper.insert(t);
            });

            return ResponseDto.successWithMsg("Create API success");
        }

    }

    @Transactional
    public ResponseDto update(ApiConfig apiConfig, List<ApiPluginConfig> pluginConfigs) {

        int size = apiConfigMapper.selectCountByPathWhenUpdate(apiConfig.getPath(), apiConfig.getId());
        if (size > 0) {
            return ResponseDto.fail("Path has been used");
        } else {

            // clean data cache if cache plugin configured before
            ApiConfig oldConfig = detail(apiConfig.getId());
            cleanDataCacheAndMetaCache(oldConfig);

            if (MediaType.APPLICATION_JSON_VALUE.equals(apiConfig.getContentType())) {
                apiConfig.setParams("[]"); // 不能设置null 前端使用会报错
            } else if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(apiConfig.getContentType())) {
                apiConfig.setJsonParam(null);
            }
            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            apiConfig.setUpdateTime(now);

            apiConfigMapper.updateById(apiConfig);
            pluginConfigMapper.deleteByApiId(apiConfig.getId());
            pluginConfigs.stream().forEach(t -> {
                pluginConfigMapper.insert(t);
            });

            return ResponseDto.successWithMsg("Update API Success");
        }

    }

    @Transactional
    public void delete(String id) {
        ApiConfig oldConfig = detail(id);
        cleanDataCacheAndMetaCache(oldConfig);
        
        apiConfigMapper.deleteById(id);
        pluginConfigMapper.deleteByApiId(id);

    }

    /**
     * 刪除API相关的元数据缓存和 API配置的插件对应的数据缓存
     *
     * @param apiConfig
     */
    private void cleanDataCacheAndMetaCache(ApiConfig apiConfig) {
        // 清除API相关的元数据缓存
        cacheManager.getCache("api").evictIfPresent(apiConfig.getPath());

        if (apiConfig.getCachePlugin() != null) {
            try {
                CachePlugin cachePlugin = PluginManager.getCachePlugin(apiConfig.getCachePlugin().getPluginName());
                cachePlugin.clean(apiConfig);
                log.debug("clean data cache when delete/update/offline api");
            } catch (Exception e) {
                log.error("clean cache failed when delete/update/offline api", e);
            }
        }
    }

    /**
     * get API full detail
     * 
     * @param id
     * @return
     */
    public ApiConfig detail(String id) {
        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        enhanceApiConfig(apiConfig);
        return apiConfig;
    }

    private void enhanceApiConfig(ApiConfig apiConfig) {
        if (apiConfig != null) {
            apiConfig.setTaskJson(JSON.parseArray(apiConfig.getTask()));
            apiConfig.setParamsJson(JSON.parseArray(apiConfig.getParams()));

            List<ApiPluginConfig> alarmPlugins = pluginConfigMapper.selectAlarmPlugins(apiConfig.getId());
            apiConfig.setAlarmPlugins(alarmPlugins);

            ApiPluginConfig cachePlugin = pluginConfigMapper.selectCachePlugin(apiConfig.getId());
            apiConfig.setCachePlugin(cachePlugin);

            ApiPluginConfig globalTransformPlugin = pluginConfigMapper.selectGlobalTransformPlugin(apiConfig.getId());
            apiConfig.setGlobalTransformPlugin(globalTransformPlugin);
        }
    }

    public List<ApiConfig> getAll() {
        List<ApiConfig> list = apiConfigMapper.selectList(null);
        List<ApiConfig> collect = list.stream().sorted(Comparator.comparing(ApiConfig::getUpdateTime).reversed())
                .collect(Collectors.toList());
        return collect;
    }

    /**
     * 给前端使用的数据格式
     *
     * @return
     */
    public JSONArray getAllApiTree() {
        List<ApiDto> list = apiConfigMapper.getAllDetail();

        Map<String, List<ApiDto>> map = list.stream().collect(Collectors.groupingBy(ApiDto::getGroupName));

        JSONArray array = new JSONArray();
        map.keySet().forEach(t -> {
            JSONObject jo = new JSONObject();
            jo.put("name", t);
            List<ApiDto> apiDtos = map.get(t);
            jo.put("children", apiDtos);
            array.add(jo);
        });
        return array;

    }

    public List<ApiConfig> search(String keyword, String field, String groupId) {
        if (StringUtils.isNoneBlank(keyword)) {
            keyword = "%" + keyword + "%";
        }
        return apiConfigMapper.selectByKeyword(keyword, field, groupId);
    }

    /**
     * servlet 从这获取API元数据
     */
    @Cacheable(value = "api", key = "#path", unless = "#result == null")
    public ApiConfig getConfig(String path) {
        ApiConfig apiConfig = apiConfigMapper.selectByPathOnline(path);
        enhanceApiConfig(apiConfig);
        return apiConfig;
    }

    public void online(String id) {
        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        apiConfig.setStatus(Constants.API_STATUS_ONLINE);
        apiConfigMapper.updateById(apiConfig);
    }

    public void offline(String id) {
        ApiConfig apiConfig = detail(id);
        cleanDataCacheAndMetaCache(apiConfig);
        apiConfig.setStatus(Constants.API_STATUS_OFFLINE);
        apiConfigMapper.updateById(apiConfig);
    }

    public String getPath(String id) {
        return apiConfigMapper.selectById(id).getPath();
    }

    public String apiDocs(List<String> ids) {
        StringBuffer temp = new StringBuffer("# 接口文档\n---\n");
        List<ApiConfig> list = apiConfigMapper.selectBatchIds(ids);
        list.stream().forEach(t -> {
            String templ = "## {0}\n- 接口地址： /{1}/{2}\n- 接口备注：{3}\n- Content-Type：{4}\n";
            temp.append(
                    MessageFormat.format(templ, t.getName(), apiContext, t.getPath(), t.getNote(), t.getContentType()));
            temp.append("\n- 请求参数：");
            if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equalsIgnoreCase(t.getContentType())) {
                String params = t.getParams();
                JSONArray array = JSON.parseArray(params);

                if (array.size() > 0) {
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("\n\n| 参数名称 | 参数类型 | 参数说明 |\n");
                    buffer.append("| :----: | :----: | :----: |\n");

                    for (int i = 0; i < array.size(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String type = jsonObject.getString("type");
                        String note = jsonObject.getString("note");
                        buffer.append(MessageFormat.format("| {0} | {1} | {2} |\n", name, type, note));
                    }

                    temp.append(buffer);
                } else {
                    temp.append("无参数\n");
                }
            } else if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(t.getContentType())) {
                temp.append("\n```json\n").append(t.getJsonParam()).append("\n```\n");
            }
            temp.append("\n---\n");
        });

        temp.append("\n导出日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return temp.toString();

    }

    /**
     * 导出API配置
     *
     * @param ids
     * @return
     */
    public JSONObject exportAPI(List<String> ids) {
        List<ApiConfig> list = apiConfigMapper.selectBatchIds(ids);
        List<ApiPluginConfig> plugins = pluginConfigMapper.selectByApiIds(ids);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("api", list);
        jsonObject.put("plugins", plugins);
        return jsonObject;
    }

    /**
     * 导入API配置
     *
     * @param configs
     * @param plugins
     */
    @Transactional
    public void importAPI(List<ApiConfig> configs, List<ApiPluginConfig> plugins) {
        configs.stream().forEach(t -> {
            t.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            t.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            t.setStatus(Constants.API_STATUS_OFFLINE);
            apiConfigMapper.insert(t);
        });

        plugins.stream().forEach(t -> {
            pluginConfigMapper.insert(t);
        });

    }
}
