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

    @Select("<script>" +
            "select * from api_config\n" +
            "<where>\n" +
            "<if test='group != null and group !=\"\"'> `group` = #{group} </if>\n" +
            "<if test='keyword != null and keyword !=\"\"'>\n" +
            "\t<if test='field != null and field !=\"\"'> and ${field} like '%'||#{keyword}||'%' </if>\n" +
            "\t<if test='field == null or field ==\"\"'> and (name like '%'||#{keyword}||'%' or note like '%'||#{keyword}||'%' or path like '%'||#{keyword}||'%' )</if>\n" +
            "</if>\n" +
            "</where>"+
            "</script>")
    List<ApiConfig> selectByKeyword(String keyword,String field,String group);

    @Select("select count(1) from api_config where path=#{path}")
    Integer selectCountByPath(String path);

    @Select("select count(1) from api_config where path=#{path} and id != #{id}")
    Integer selectCountByPathWhenUpdate(String path, Integer id);

    @Select("select count(1) from api_config where datasource_id = #{id}")
    int countByDatasoure(Integer id);

    @Select("select count(1) from api_config where `group` = #{id}")
    int selectCountByGroup(Integer id);
}
