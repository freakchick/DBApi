package com.gitee.freakchicken.dbapi.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.basic.domain.Client;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClientMapper extends BaseMapper<Client> {

    @Select("select * from client where id = #{id} and secret = #{secret} ")
    Client selectByIdSecret(@Param("id") String id, @Param("secret") String secret);

    @Select("select * from client where token = #{token}")
    Client selectByToken(String token);
}