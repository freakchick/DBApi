package com.gitee.freakchicken.dbapi.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface IPMapper {

    @Update("update firewall set status = 'off' ")
    void turnoff();

    @Update("update firewall set status = 'on' , mode = #{mode}")
    void turnOn(String mode);

    @Update("update ip_rules set ip = #{ip} where type = #{type}")
    void saveIP(@Param("ip") String ip, @Param("type") String type);

    @Select("select status, mode from firewall")
    Map<String, String> getStatus();

    @Select("select type, ip from ip_rules")
    List<Map<String, String>> getIPRule();

}
