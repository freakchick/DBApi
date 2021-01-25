package com.jq.dbapi.service;

import com.jq.dbapi.dao.ApiConfigMapper;
import com.jq.dbapi.domain.ApiConfig;
import com.jq.dbapi.util.ResponseDto;
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
        return apiConfigMapper.selectByPathOnline(path);
    }

    public void online(Integer id) {
        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        apiConfig.setStatus(1);
        apiConfigMapper.updateById(apiConfig);
    }

    public void offline(Integer id) {

        ApiConfig apiConfig = apiConfigMapper.selectById(id);
        apiConfig.setStatus(0);
        apiConfigMapper.updateById(apiConfig);
    }
}
