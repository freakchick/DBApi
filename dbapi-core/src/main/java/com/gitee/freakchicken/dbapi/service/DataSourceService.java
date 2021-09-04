package com.gitee.freakchicken.dbapi.service;

import com.gitee.freakchicken.dbapi.dao.ApiConfigMapper;
import com.gitee.freakchicken.dbapi.dao.DataSourceMapper;
import com.gitee.freakchicken.dbapi.domain.DataSource;
import com.gitee.freakchicken.dbapi.util.PoolManager;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: dbApi
 * @description:
 * @author:
 * @create: 2021-01-20 10:43
 **/
@Service
@Slf4j
public class DataSourceService {

    @Autowired
    DataSourceMapper dataSourceMapper;
    @Autowired
    ApiConfigMapper apiConfigMapper;

    @Transactional
    public void add(DataSource dataSource) {
        dataSource.setId(UUIDUtil.id());
        dataSource.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        dataSource.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        dataSourceMapper.insert(dataSource);
    }

    @CacheEvict(value = "datasource", key = "#dataSource.id")
    @Transactional
    public void update(DataSource dataSource) {
        dataSource.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        dataSourceMapper.updateById(dataSource);
    }

    @CacheEvict(value = "datasource", key = "#id")
    @Transactional
    public ResponseDto delete(String id) {
        int i = apiConfigMapper.countByDatasoure(id);
        if (i == 0) {
            dataSourceMapper.deleteById(id);
            PoolManager.removeJdbcConnectionPool(id);
            return ResponseDto.successWithMsg("删除成功");
        } else {
            return ResponseDto.fail("该数据源已经被使用，不可删除");
        }
    }

    @Cacheable(value = "datasource", key = "#id", unless = "#result == null")
    public DataSource detail(String id) {
        DataSource dataSource = dataSourceMapper.selectById(id);
        return dataSource;
    }

    public List<DataSource> getAll() {
        List<DataSource> list = dataSourceMapper.selectList(null);
        List<DataSource> collect = list.stream().sorted(Comparator.comparing(DataSource::getUpdateTime).reversed()).collect(Collectors.toList());
        return collect;
    }

    public String getDBType(Integer id) {
        return dataSourceMapper.selectById(id).getType();
    }

    public List<DataSource> selectBatch(List<String> ids) {
        List<DataSource> dataSources = dataSourceMapper.selectBatchIds(ids);
        return dataSources;
    }

    @Transactional
    public void insertBatch(List<DataSource> list) {
        list.forEach(t -> {
            t.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            dataSourceMapper.insert(t);
        });
    }
}
