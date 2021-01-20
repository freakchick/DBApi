package com.jq.dbapi.service;

import com.jq.dbapi.dao.DataSourceMapper;
import com.jq.dbapi.domain.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-20 10:43
 **/
@Service
@Slf4j
public class DataSourceService {

    @Autowired
    DataSourceMapper dataSourceMapper;

    @Transactional
    public void add(DataSource dataSource) {
        dataSourceMapper.insert(dataSource);
    }

    @Transactional
    public void update(DataSource dataSource) {
        dataSourceMapper.updateById(dataSource);
    }

    @Transactional
    public void delete(Integer id) {
        dataSourceMapper.deleteById(id);
    }

    public DataSource detail(Integer id) {
        DataSource dataSource = dataSourceMapper.selectById(id);
        return dataSource;
    }

    public List<DataSource> getAll() {
        List<DataSource> list = dataSourceMapper.selectList(null);
        return list;
    }
}
