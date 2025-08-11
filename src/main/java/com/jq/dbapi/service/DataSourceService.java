package com.jq.dbapi.service;

import com.jq.dbapi.dao.ApiConfigMapper;
import com.jq.dbapi.dao.DataSourceMapper;
import com.jq.dbapi.domain.DataSource;
import com.jq.dbapi.util.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class DataSourceService {

    @Autowired
    DataSourceMapper dataSourceMapper;
    @Autowired
    ApiConfigMapper apiConfigMapper;

    @Transactional
    public void add(DataSource dataSource) {
        dataSourceMapper.insert(dataSource);
    }

    @Transactional
    public void update(DataSource dataSource) {
        dataSourceMapper.updateById(dataSource);
    }

    @CacheEvict(value = "datasource", key = "#id")
    @Transactional
    public ResponseDto delete(Integer id) {
        int i = apiConfigMapper.countByDatasoure(id);
        if (i==0){
            dataSourceMapper.deleteById(id);
            return ResponseDto.success("删除成功");
        }else{
            return ResponseDto.fail("该数据源已经被使用，不可删除");
        }
    }

    @Cacheable(value = "datasource", key = "#id", unless = "#result == null")
    public DataSource detail(Integer id) {
        DataSource dataSource = dataSourceMapper.selectById(id);
        return dataSource;
    }

    public List<DataSource> getAll() {
        List<DataSource> list = dataSourceMapper.selectList(null);
        return list;
    }
}
