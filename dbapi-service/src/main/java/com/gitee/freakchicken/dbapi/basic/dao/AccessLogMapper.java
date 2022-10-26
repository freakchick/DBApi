package com.gitee.freakchicken.dbapi.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccessLogMapper extends BaseMapper<AccessLog> {


    @Select("SELECT FROM_UNIXTIME(timestamp,'%Y-%m-%d') date,count(1) num from access_log where FROM_UNIXTIME(timestamp,'%Y-%m-%d') >= #{start} and FROM_UNIXTIME(timestamp,'%Y-%m-%d') < #{end} group by FROM_UNIXTIME(timestamp,'%Y-%m-%d')\n")
    public List<Map<String,Object>> countByDate(@Param("start") String start, @Param("end") String end);

}
