package com.gitee.freakchicken.dbapi.basic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.gitee.freakchicken.dbapi.basic.dao.ApiConfigMapper;
import com.gitee.freakchicken.dbapi.basic.dao.DataSourceMapper;
import com.gitee.freakchicken.dbapi.basic.domain.DataSource;
import com.gitee.freakchicken.dbapi.basic.util.DESUtils;
import com.gitee.freakchicken.dbapi.basic.util.PoolManager;
import com.gitee.freakchicken.dbapi.basic.util.UUIDUtil;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
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
@DS("meta-db")
public class DataSourceService {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    DataSourceMapper dataSourceMapper;
    @Autowired
    ApiConfigMapper apiConfigMapper;

    @Transactional
    public void add(DataSource dataSource) {
        dataSource.setId(UUIDUtil.id());
        dataSource.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        dataSource.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        // 新增数据源对密码加密
        try {
            dataSource.setPassword(DESUtils.encrypt(dataSource.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataSourceMapper.insert(dataSource);
    }

    // @CacheEvict(value = "datasource", key = "#dataSource.id")
    @Transactional
    public void update(DataSource dataSource) {
        dataSource.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        // 如果修改了密码, 需要对密码加密
        if (dataSource.isEdit_password()) {
            try {
                dataSource.setPassword(DESUtils.encrypt(dataSource.getPassword()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dataSourceMapper.updateById(dataSource);
        PoolManager.removeJdbcConnectionPool(dataSource.getId());
        cacheManager.getCache("datasource").evictIfPresent(dataSource.getId());

    }

    /**
     * @param id
     * @return
     */
    @Transactional
    public ResponseDto delete(String id) {
        List<ApiConfig> list = apiConfigMapper.selectList(null);
        List<String> str = list.stream().filter(t -> {
            String task = t.getTask();
            JSONArray array = JSON.parseArray(task);
            for (int i = 0; i < array.size(); i++) {
                JSONObject jo = array.getJSONObject(i);
                String datasourceId = jo.getString("datasourceId");
                if (id.equals(datasourceId)) {
                    return true;
                }
            }
            return false;
        }).map(item -> item.getName() + "(" + item.getId() + ")").collect(Collectors.toList());

        if (list.size() == 0) {
            dataSourceMapper.deleteById(id);

            PoolManager.removeJdbcConnectionPool(id);
            cacheManager.getCache("datasource").evictIfPresent(id);

            return ResponseDto.successWithMsg("Datasource delete success");
        } else {
            return ResponseDto.fail("Can not delete! Used by API: " + str.stream().collect(Collectors.joining(";")));
        }
    }

    @Cacheable(value = "datasource", key = "#id", unless = "#result == null")
    public DataSource detail(String id) {
        DataSource dataSource = dataSourceMapper.selectById(id);
        return dataSource;
    }

    public List<DataSource> getAll() {
        List<DataSource> list = dataSourceMapper.selectList(null);
        List<DataSource> collect = list.stream().sorted(Comparator.comparing(DataSource::getUpdateTime).reversed())
                .collect(Collectors.toList());
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
