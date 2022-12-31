package com.gitee.freakchicken.dbapi.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiSql;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ApiSqlMapper extends BaseMapper<ApiSql> {


    @Delete("delete from api_sql where api_id = #{id}")
    void deleteByApiID(String id);

    @Select("select * from api_sql where api_id = #{id}")
    List<ApiSql> selectByApiId(String id);

    @Select("<script>select api_id,sql_text,transform_plugin,transform_plugin_params from api_sql where api_id in <foreach open=\"(\" close=\")\" collection=\"ids\" separator=\",\" item=\"item\" index=\"index\">#{item}</foreach></script>")
    List<ApiSql> selectByApiIds(@Param("ids") List<String> ids);
}
