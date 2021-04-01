package com.jq.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jq.dbapi.domain.ApiConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ApiConfigMapper extends BaseMapper<ApiConfig> {

    @Select("select * from api_config where path=#{path} and status = 1")
    ApiConfig selectByPathOnline(String path);

    @Select("<script>select * from api_config " +
            "<if test='field != null and field !=\"\"'> where ${field} like #{keyword} </if> " +
            "<if test='field == null or field==\"\"'> where name like #{keyword} or note like #{keyword} or path like #{keyword}</if> " +
            "</script>")
    List<ApiConfig> selectByKeyword(String keyword,String field);

    @Select("select count(1) from api_config where path=#{path}")
    Integer selectCountByPath(String path);

    @Select("select count(1) from api_config where path=#{path} and id != #{id}")
    Integer selectCountByPathWhenUpdate(String path, Integer id);

    @Select("select count(1) from api_config where datasource_id = #{id}")
    int countByDatasoure(Integer id);
}
