package com.gitee.freakchicken.dbapi.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.gitee.freakchicken.dbapi.basic.dao.ApiConfigMapper;
import com.gitee.freakchicken.dbapi.basic.dao.CacheConfigMapper;
import com.gitee.freakchicken.dbapi.basic.dao.DataSourceMapper;
import com.gitee.freakchicken.dbapi.basic.dao.AlarmMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiAlarmConfig;
import com.gitee.freakchicken.dbapi.basic.domain.ApiCacheConfig;
import com.gitee.freakchicken.dbapi.basic.domain.ApiDto;

import com.gitee.freakchicken.dbapi.basic.util.UUIDUtil;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.plugin.CachePlugin;
import com.gitee.freakchicken.dbapi.plugin.PluginManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    CacheConfigMapper cacheMapper;
    @Autowired
    AlarmMapper alarmMapper;
    @Autowired
    CacheManager cacheManager;

    @Transactional
    public ResponseDto add(ApiConfig apiConfig, ApiAlarmConfig alarm, ApiCacheConfig apiCache) {
        int size = apiConfigMapper.selectCountByPath(apiConfig.getPath());
        if (size > 0) {
            return ResponseDto.fail("Path has been used!");
        } else {
            apiConfig.setStatus(0);
            String id = UUIDUtil.id();
            apiConfig.setId(id);

            if (MediaType.APPLICATION_JSON_VALUE.equals(apiConfig.getContentType())) {
                apiConfig.setParams("[]"); // 不能设置null 前端使用会报错
            } else if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(apiConfig.getContentType())) {
                apiConfig.setJsonParam(null);
            }

            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            apiConfig.setCreateTime(now);
            apiConfig.setUpdateTime(now);
            apiConfigMapper.insert(apiConfig);

            if (alarm != null && StringUtils.isNotBlank(alarm.getPluginName())) {
                alarm.setApiId(id);
                alarmMapper.insert(alarm);
            }
            if (apiCache != null && StringUtils.isNotBlank(alarm.getPluginName())) {
                apiCache.setApiId(id);
                cacheMapper.insert(apiCache);
            }
            return ResponseDto.successWithMsg("create API success");
        }

    }

    @Transactional
    public ResponseDto update(ApiConfig apiConfig, ApiAlarmConfig alarm, ApiCacheConfig apiCache) {

        int size = apiConfigMapper.selectCountByPathWhenUpdate(apiConfig.getPath(), apiConfig.getId());
        if (size > 0) {
            return ResponseDto.fail("Path has been used");
        } else {
            ApiConfig oldConfig = apiConfigMapper.selectById(apiConfig.getId());
            apiConfig.setStatus(0);
            apiConfig.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            if (MediaType.APPLICATION_JSON_VALUE.equals(apiConfig.getContentType())) {
                apiConfig.setParams("[]"); // 不能设置null 前端使用会报错
            } else if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(apiConfig.getContentType())) {
                apiConfig.setJsonParam(null);
            }
            apiConfigMapper.updateById(apiConfig);

            alarmMapper.deleteByApiId(apiConfig.getId());
            if (alarm != null) {
                alarmMapper.insert(alarm);
            }
            ApiCacheConfig oldCacheConfig = cacheMapper.selectByApiId(apiConfig.getId());
            cacheMapper.deleteByApiId(apiConfig.getId());
            if (apiCache != null) {
                cacheMapper.insert(apiCache);
            }

            cleanDataCacheAndMetaCache(oldConfig, oldCacheConfig);

            return ResponseDto.successWithMsg("Update API Success");
        }

    }

    @Transactional
    public void delete(String id) {
        ApiConfig oldConfig = apiConfigMapper.selectById(id);
        ApiCacheConfig oldCacheConfig = cacheMapper.selectByApiId(id);

        apiConfigMapper.deleteById(id);
        alarmMapper.deleteByApiId(id);
        cacheMapper.deleteByApiId(id);

        cleanDataCacheAndMetaCache(oldConfig, oldCacheConfig);
    }

    /**
     * 刪除API相关的元数据缓存和 API配置的插件对应的数据缓存
     * 
     * @param apiConfig
     * @param apiCache
     */
    public void cleanDataCacheAndMetaCache(ApiConfig apiConfig, ApiCacheConfig apiCache) {
        // 清除API相关的元数据缓存
        cacheManager.getCache("api").evictIfPresent(apiConfig.getPath());
        cacheManager.getCache("api_alarm_config").evictIfPresent(apiConfig.getId());
        cacheManager.getCache("api_cache_config").evictIfPresent(apiConfig.getId());

        if (apiCache != null) {
            try {
                CachePlugin cachePlugin = PluginManager.getCachePlugin(apiCache.getPluginName());
                cachePlugin.clean(apiConfig);
                log.debug("clean data cache when delete api");
            } catch (Exception e) {
                log.error("clean cache failed when delete api", e);
            }
        }
    }

    public ApiConfig detail(String id) {
        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        return apiConfig;
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
    public JSONArray getAllDetail() {
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
        if (Objects.isNull(apiConfig)) {
            log.warn("can't get [{}] api config from db", path);
            return null;
        }
        return apiConfig;
    }

    public void online(String id) {
        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        apiConfig.setStatus(1);
        apiConfigMapper.updateById(apiConfig);
    }

    public void offline(String id) {

        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        ApiCacheConfig cacheConfig = cacheMapper.selectByApiId(id);

        apiConfig.setStatus(0);
        apiConfigMapper.updateById(apiConfig);

        cleanDataCacheAndMetaCache(apiConfig, cacheConfig);

    }

    public String getPath(String id) {
        return apiConfigMapper.selectById(id).getPath();
    }

    public String apiDocs(List<String> ids) {
        StringBuffer temp = new StringBuffer("# 接口文档\n---\n");
        List<ApiConfig> list = apiConfigMapper.selectBatchIds(ids);
        list.stream().forEach(t -> {
            temp.append("## ").append(t.getName()).append("\n- 接口地址： /api/").append(t.getPath())
                    .append("\n- 接口备注：").append(t.getNote());
            temp.append("\n- Content-Type：").append(t.getContentType());

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
                        if (type.startsWith("Array")) {
                            type = type.substring(6, type.length() - 1) + "数组";
                        }
                        String note = jsonObject.getString("note");
                        buffer.append("|").append(name).append("|").append(type).append("|").append(note).append("|\n");
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

    public JSONObject selectBatch(List<String> ids) {
        List<ApiConfig> list = apiConfigMapper.selectBatchIds(ids);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("api", list);

        return jsonObject;
    }

    @Transactional
    public void insertBatch(List<ApiConfig> configs) {
        configs.stream().forEach(t -> {
            t.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            t.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            t.setStatus(0);
            apiConfigMapper.insert(t);
        });

    }
}
