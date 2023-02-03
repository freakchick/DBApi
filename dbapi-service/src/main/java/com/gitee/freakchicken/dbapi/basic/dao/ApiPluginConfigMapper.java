package com.gitee.freakchicken.dbapi.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiPluginConfig;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ApiPluginConfigMapper extends BaseMapper<ApiPluginConfig> {


    @Select("select * from api_plugin_config where api_id = #{apiId} and plugin_type='alarm' ")
    List<ApiPluginConfig> selectAlarmPlugin(String apiId);

    @Select("select * from api_plugin_config where api_id = #{apiId} and plugin_type='cache' ")
    ApiPluginConfig selectCachePlugin(String apiId);

    @Select("select * from api_plugin_config where api_id = #{apiId} and plugin_type='conversion' ")
    ApiPluginConfig selectConversionPlugin(String apiId);

    @Delete("delete from api_plugin_config where api_id = #{apiId} ")
    int deleteByApiId(String apiId);

    @Select("<script>select * from api_plugin_config where api_id in " +
    "<foreach open=\"(\" close=\")\" collection=\"ids\" separator=\",\" item=\"item\" index=\"index\">#{item}</foreach></script>")
    List<ApiPluginConfig> selectByApiIds(List<String> ids);
}