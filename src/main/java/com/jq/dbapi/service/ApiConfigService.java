package com.jq.dbapi.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jq.dbapi.dao.ApiConfigMapper;
import com.jq.dbapi.dao.DataSourceMapper;
import com.jq.dbapi.domain.ApiDto;
import com.jq.dbapi.domain.ApiConfig;
import com.jq.dbapi.util.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
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

    @Transactional
    public ResponseDto add(ApiConfig apiConfig) {

        int size = apiConfigMapper.selectCountByPath(apiConfig.getPath());
        if (size > 0) {
            return ResponseDto.fail("该路径已被使用，请修改请求路径再保存");
        } else {
            apiConfig.setStatus(0);
            apiConfigMapper.insert(apiConfig);
            return ResponseDto.success("添加成功");
        }

    }

    @CacheEvict(value = "api", key = "#apiConfig.path")
    @Transactional
    public ResponseDto update(ApiConfig apiConfig) {

        int size = apiConfigMapper.selectCountByPathWhenUpdate(apiConfig.getPath(), apiConfig.getId());
        if (size > 0) {
            return ResponseDto.fail("该路径已被使用，请修改请求路径再保存");
        } else {
            apiConfig.setStatus(0);
            apiConfigMapper.updateById(apiConfig);
            return ResponseDto.success("修改成功");
        }

    }

    @CacheEvict(value = "api", key = "#path")
    @Transactional
    public void delete(Integer id, String path) {
        apiConfigMapper.deleteById(id);
    }

    public ApiConfig detail(Integer id) {
        return apiConfigMapper.selectById(id);
    }

    public List<ApiConfig> getAll() {
        return apiConfigMapper.selectList(null);
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
        return apiConfigMapper.selectByKeyword(keyword, field, groupId);
    }

    @Cacheable(value = "api", key = "#path", unless = "#result == null")
    public ApiConfig getConfig(String path) {
        log.info("执行sql查询api参数");
        return apiConfigMapper.selectByPathOnline(path);
    }

    @CacheEvict(value = "api", key = "#path")
    public void online(int id, String path) {
        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        apiConfig.setStatus(1);
        apiConfigMapper.updateById(apiConfig);
    }

    @CacheEvict(value = "api", key = "#path")
    public void offline(int id, String path) {

        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        apiConfig.setStatus(0);
        apiConfigMapper.updateById(apiConfig);
    }

    public String getPath(Integer id) {
        return apiConfigMapper.selectById(id).getPath();
    }

    public String apiDocs(List<Integer> ids) {
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

}
