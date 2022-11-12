package com.gitee.freakchicken.dbapi.basic.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccessLogMapper extends BaseMapper<AccessLog> {


    @Select("select date,sum(success) successNum, sum(fail) failNum from (SELECT FROM_UNIXTIME(timestamp,'%Y-%m-%d') date,case when status=200 then 1 else 0 end as success,case when status!=200 then 1 else 0 end as fail from access_log where timestamp >= #{start} and timestamp < #{end}) group by date")
    public List<JSONObject> countByDate(@Param("start") long start, @Param("end") long end);

    @Select("select url,num from (select url,count(1) num from access_log where timestamp >= #{start} and timestamp < #{end} group by url) order by num desc limit 0,10")
    public List<JSONObject> top5api(@Param("start") long start, @Param("end") long end);


    @Select("select app_id,num from (select app_id,count(1) num from access_log where timestamp >= #{start} and timestamp < #{end} and app_id is not null and app_id != '' group by app_id) order by num desc limit 0,10")
    public List<JSONObject> top5app(@Param("start") long start, @Param("end") long end);

    @Select("select ip,num from (select ip,count(1) num from access_log where timestamp >= #{start} and timestamp < #{end} and app_id is not null group by ip) order by num desc limit 0,10")
    public List<JSONObject> topNIP(@Param("start") long start, @Param("end") long end);


    @Select("select url,duration from (select url,round(avg(duration)) duration from access_log where timestamp >= #{start} and timestamp < #{end} group by url) order by duration desc limit 0,10")
    public List<JSONObject> top5duration(@Param("start") long start, @Param("end") long end);

    @Select("select sum(success) successNum,sum(fail) failNum from (select case when status=200 then 1 else 0 end as success,case when status!=200 then 1 else 0 end as fail from access_log where timestamp >= #{start} and timestamp < #{end} )")
    public JSONObject successRatio(@Param("start") long start, @Param("end") long end);

    @Select("<script>select * from default.access_log where timestamp between #{start} and #{end} <if test=\"status != null and status !=''\" > and status = #{status}</if><if test=\"ip != null and ip !=''\" > and ip = #{ip}</if><if test=\"url != null and url !=''\" > and url = #{url}</if><if test=\"appId != null and appId !=''\" > and app_id = #{appId}</if></script>")
    List<AccessLog> search(@Param("url") String url, @Param("appId") String appId, @Param("start") Long start, @Param("end") Long end, @Param("status") Integer status,@Param("ip") String ip) ;


}
