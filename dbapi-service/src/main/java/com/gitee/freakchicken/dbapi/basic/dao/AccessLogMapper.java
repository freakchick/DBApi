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


    @Select("select date,status,count(1) num from (SELECT FROM_UNIXTIME(timestamp,'%Y-%m-%d') date,case when status=200 then 'success' else 'fail' end as status from access_log where timestamp >= #{start} and timestamp < #{end}) group by date,status")
    public List<Map<String,Object>> countByDate(@Param("start") long start, @Param("end") long end);

    @Select("select url,num from (select url,count(1) num from access_log where timestamp >= #{start} and timestamp < #{end} group by url) order by url limit 0,5")
    public List<Map<String,Object>> top5api(@Param("start") long start, @Param("end") long end);


    @Select("select app_id,num from (select app_id,count(1) num from access_log where timestamp >= #{start} and timestamp < #{end} group by app_id) order by app_id limit 0,5")
    public List<Map<String,Object>> top5app(@Param("start") long start, @Param("end") long end);

    @Select("select status,count(1) num from (select case when status=200 then 'success' else 'fail' end as status from access_log where timestamp >= #{start} and timestamp < #{end} ) group by status")
    public List<Map<String,Object>> successRatio(@Param("start") long start, @Param("end") long end);
}
