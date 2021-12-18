package com.gitee.freakchicken.dbapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.common.ApiSql;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.dao.ApiConfigMapper;
import com.gitee.freakchicken.dbapi.dao.ApiSqlMapper;
import com.gitee.freakchicken.dbapi.dao.DataSourceMapper;
import com.gitee.freakchicken.dbapi.domain.ApiDto;
import com.gitee.freakchicken.dbapi.plugin.CachePlugin;
import com.gitee.freakchicken.dbapi.plugin.PluginManager;
import com.gitee.freakchicken.dbapi.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-19 17:27
 **/
@Slf4j
@Service
public class ApiConfigService {

    @Autowired
    ApiConfigMapper apiConfigMapper;

    @Autowired
    DataSourceMapper dataSourceMapper;

    @Autowired
    ApiSqlMapper apiSqlMapper;

    @Transactional
    public ResponseDto add(ApiConfig apiConfig) {

        int size = apiConfigMapper.selectCountByPath(apiConfig.getPath());
        if (size > 0) {
            return ResponseDto.fail("Path has been used!");
        } else {
            apiConfig.setStatus(0);
            apiConfig.setId(UUIDUtil.id());

            String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            apiConfig.setCreateTime(now);
            apiConfig.setUpdateTime(now);
            apiConfigMapper.insert(apiConfig);
            apiConfig.getSqlList().stream().forEach(t -> {
                t.setApiId(apiConfig.getId());
                apiSqlMapper.insert(t);
            });

            return ResponseDto.successWithMsg("create success");
        }

    }

    @CacheEvict(value = "api", key = "#apiConfig.path")
    @Transactional
    public ResponseDto update(ApiConfig apiConfig) {

        int size = apiConfigMapper.selectCountByPathWhenUpdate(apiConfig.getPath(), apiConfig.getId());
        if (size > 0) {
            return ResponseDto.fail("Path has been used");
        } else {
            ApiConfig oldConfig = apiConfigMapper.selectById(apiConfig.getId());
            apiConfig.setStatus(0);
            apiConfig.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            apiConfigMapper.updateById(apiConfig);
            apiSqlMapper.deleteByApiID(apiConfig.getId());
            apiConfig.getSqlList().stream().forEach(t -> {
                t.setApiId(apiConfig.getId());
                apiSqlMapper.insert(t);
            });

            //清除所有缓存
            if (StringUtils.isNoneBlank(apiConfig.getCachePlugin())) {
                try {
                    CachePlugin cachePlugin = PluginManager.getCachePlugin(oldConfig.getCachePlugin());
                    cachePlugin.clean(oldConfig);
                    log.debug("update api config, then clean cache");
                } catch (Exception e) {
                    log.error("clean cache failed when update api", e);
                }
            }
            return ResponseDto.successWithMsg("update success");
        }

    }

    @CacheEvict(value = "api", key = "#path")
    @Transactional
    public void delete(String id, String path) {
        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        apiConfigMapper.deleteById(id);
        apiSqlMapper.deleteByApiID(id);
        //清除所有缓存
        if (StringUtils.isNoneBlank(apiConfig.getCachePlugin())) {
            try {
                CachePlugin cachePlugin = PluginManager.getCachePlugin(apiConfig.getCachePlugin());
                cachePlugin.clean(apiConfig);
                log.debug("delete api then clean cache");
            } catch (Exception e) {
                log.error("clean cache failed when delete api", e);
            }
        }
    }

    public ApiConfig detail(String id) {
        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        List<ApiSql>  list = apiSqlMapper.selectByApiId(apiConfig.getId());
//        List<String> collect = list.stream().map(t -> t.getSql()).collect(Collectors.toList());
        apiConfig.setSqlList(list);
        return apiConfig;
    }

    public List<ApiConfig> getAll() {
        List<ApiConfig> list = apiConfigMapper.selectList(null);
        List<ApiConfig> collect = list.stream().sorted(Comparator.comparing(ApiConfig::getUpdateTime).reversed()).collect(Collectors.toList());
        return collect;
    }

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

    @Cacheable(value = "api", key = "#path", unless = "#result == null")
    public ApiConfig getConfig(String path) {
        ApiConfig apiConfig = apiConfigMapper.selectByPathOnline(path);
        List<ApiSql> apiSqls = apiSqlMapper.selectByApiId(apiConfig.getId());
        apiConfig.setSqlList(apiSqls);
        return apiConfig;
    }

    @CacheEvict(value = "api", key = "#path")
    public void online(String id, String path) {
        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        apiConfig.setStatus(1);
        apiConfigMapper.updateById(apiConfig);
    }

    @CacheEvict(value = "api", key = "#path")
    public void offline(String id, String path) {

        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        apiConfig.setStatus(0);
        apiConfigMapper.updateById(apiConfig);
        if (StringUtils.isNoneBlank(apiConfig.getCachePlugin())) {
            try {
                CachePlugin cachePlugin = PluginManager.getCachePlugin(apiConfig.getCachePlugin());
                cachePlugin.clean(apiConfig);
                log.debug("offline api then clean cache");
            } catch (Exception e) {
                log.error("clean cache error", e);
            }
        }
    }

    public String getPath(String id) {
        return apiConfigMapper.selectById(id).getPath();
    }

    public String apiDocs(List<String> ids) {
        StringBuffer temp = new StringBuffer("# 接口文档\n---\n");
        List<ApiConfig> list = apiConfigMapper.selectBatchIds(ids);
        list.stream().forEach(t -> {
            temp.append("## ").append(t.getName()).append("\n- 接口地址： /api/").append(t.getPath())
                    .append("\n- 接口备注：").append(t.getNote()).append("\n- 请求参数：");

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
            temp.append("\n---\n");
        });

        temp.append("\n导出日期：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return temp.toString();

    }

    public List<ApiConfig> selectBatch(List<String> ids) {
        List<ApiConfig> list = apiConfigMapper.selectBatchIds(ids);
        return list;
    }

    @Transactional
    public void insertBatch(List<ApiConfig> configs) {
        configs.stream().forEach(t -> {
            t.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            t.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            apiConfigMapper.insert(t);
        });
    }
}
