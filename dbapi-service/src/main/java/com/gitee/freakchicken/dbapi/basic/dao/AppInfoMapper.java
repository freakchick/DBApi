package com.gitee.freakchicken.dbapi.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.basic.domain.AppInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AppInfoMapper extends BaseMapper<AppInfo> {

    @Select("select * from app_info where id = #{id} and secret = #{secret} ")
    AppInfo selectByIdSecret(@Param("id") String id, @Param("secret") String secret);

    @Select("select * from app_info where token = #{token}")
    AppInfo selectByToken(String token);
}