package com.jq.dbapi.service;

import com.jq.dbapi.dao.ApiConfigMapper;
import com.jq.dbapi.dao.DataSourceMapper;
import com.jq.dbapi.domain.ApiConfig;
import com.jq.dbapi.util.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<ApiConfig> search(String keyword, String field, String group) {
        return apiConfigMapper.selectByKeyword(keyword, field, group);
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

}
