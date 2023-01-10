package com.gitee.freakchicken.dbapi.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiAlarmConfig;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AlarmMapper extends BaseMapper<ApiAlarmConfig> {


    @Select("select * from api_alarm where api_id = #{apiId} ")
    List<ApiAlarmConfig> selectByApiId(String apiId);

    @Delete("delete from api_alarm where api_id = #{apiId} ")
    int deleteByApiId(String apiId);
}