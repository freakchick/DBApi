package com.gitee.freakchicken.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.common.ApiSql;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ApiSqlMapper extends BaseMapper<ApiSql> {


    @Delete("delete from api_sql where api_id = #{id}")
    void deleteByApiID(String id);

    @Select("select * from api_sql where api_id = #{id}")
    List<ApiSql> selectByApiId(String id);
}
