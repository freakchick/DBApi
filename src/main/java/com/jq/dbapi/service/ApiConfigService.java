package com.jq.dbapi.service;

import com.jq.dbapi.dao.ApiConfigMapper;
import com.jq.dbapi.domain.ApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-19 17:27
 **/
@Service
public class ApiConfigService {

    @Autowired
    ApiConfigMapper apiConfigMapper;

    @Transactional
    public void add(ApiConfig apiConfig) {
        apiConfig.setStatus(0);
        apiConfigMapper.insert(apiConfig);
    }

    @Transactional
    public void update(ApiConfig apiConfig) {
        apiConfigMapper.updateById(apiConfig);
    }

    @Transactional
    public void delete(Integer id) {
        apiConfigMapper.deleteById(id);
    }

    public ApiConfig detail(Integer id) {
        return apiConfigMapper.selectById(id);
    }

    public List<ApiConfig> getAll() {
        return apiConfigMapper.selectList(null);
    }

    public ApiConfig getConfig(String path) {
       return  apiConfigMapper.selectByPath(path);
    }
}
