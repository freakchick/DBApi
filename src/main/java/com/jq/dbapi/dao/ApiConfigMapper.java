package com.jq.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jq.dbapi.domain.ApiConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ApiConfigMapper extends BaseMapper<ApiConfig> {

    @Select("select * from api_config where path=#{path} and status = 1")
    ApiConfig selectByPath(String path);

    @Select("select count(1) from api_config where datasource_id = #{id}")
    int countByDatasoure(Integer id);
}
