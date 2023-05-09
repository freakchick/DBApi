package com.gitee.freakchicken.dbapi.basic.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
@DS("access-log-db")

public interface AccessLogMapper extends BaseMapper<AccessLog> {


    @Select("select date,sum(success) successNum, sum(fail) failNum from (SELECT FROM_UNIXTIME(timestamp,'%Y-%m-%d') date,case when status=200 then 1 else 0 end as success,case when status!=200 then 1 else 0 end as fail from access_log where timestamp >= #{start} and timestamp < #{end}) a group by date")
    public List<JSONObject> countByDate(@Param("start") long start, @Param("end") long end);

    @Select("select url,num from (select url,count(1) num from access_log where timestamp >= #{start} and timestamp < #{end} group by url) a order by num desc limit 0,10")
    public List<JSONObject> top5api(@Param("start") long start, @Param("end") long end);


    @Select("select client_id,num from (select client_id,count(1) num from access_log where timestamp >= #{start} and timestamp < #{end} and client_id is not null and client_id != '' group by client_id) a order by num desc limit 0,10")
    public List<JSONObject> top5app(@Param("start") long start, @Param("end") long end);

    @Select("select ip,num from (select ip,count(1) num from access_log where timestamp >= #{start} and timestamp < #{end} group by ip) a order by num desc limit 0,10")
    public List<JSONObject> topNIP(@Param("start") long start, @Param("end") long end);

    @Select("select url,duration from (select url,round(avg(duration)) duration from access_log where timestamp >= #{start} and timestamp < #{end} group by url) a order by duration desc limit 0,10")
    public List<JSONObject> top5duration(@Param("start") long start, @Param("end") long end);

    @Select("select sum(success) successNum,sum(fail) failNum from (select case when status=200 then 1 else 0 end as success,case when status!=200 then 1 else 0 end as fail from access_log where timestamp >= #{start} and timestamp < #{end} ) a")
    public JSONObject successRatio(@Param("start") long start, @Param("end") long end);

    @Select("<script>select * from access_log where timestamp between #{start} and #{end} <if test=\"status != null and status !=''\" > and status = #{status}</if><if test=\"ip != null and ip !=''\" > and ip = #{ip}</if><if test=\"url != null and url !=''\" > and url = #{url}</if><if test=\"clientId != null and clientId !=''\" > and client_id = #{clientId}</if></script>")
    List<AccessLog> search(@Param("url") String url, @Param("clientId") String clientId, @Param("start") Long start, @Param("end") Long end, @Param("status") Integer status,@Param("ip") String ip) ;


}
