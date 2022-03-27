package com.gitee.freakchicken.dbapi.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.common.Alarm;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AlarmMapper extends BaseMapper<Alarm> {


    @Select("select mail from api_alarm where api_id = #{apiId} ")
    String selectMailByApiId(String apiId);

    @Delete("delete from api_alarm where api_id = #{apiId} ")
    int deleteByApiID(String apiId);
}