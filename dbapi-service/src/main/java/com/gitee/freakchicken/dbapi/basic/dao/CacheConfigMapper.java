package com.gitee.freakchicken.dbapi.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiCacheConfig;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CacheConfigMapper extends BaseMapper<ApiCacheConfig> {

    @Select("select * from api_cache where api_id = #{apiId} ")
    ApiCacheConfig selectByApiId(String apiId);

    @Delete("delete from api_cache where api_id = #{apiId} ")
    int deleteByApiId(String apiId);


}
