package com.gitee.freakchicken.dbapi.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.basic.domain.Client;
import com.gitee.freakchicken.dbapi.basic.domain.ClientToken;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ClientMapper extends BaseMapper<Client> {

    @Select("select * from client where id = #{id} and secret = #{secret} ")
    Client selectByIdSecret(@Param("id") String id, @Param("secret") String secret);

    @Select("select * from client where token = #{token}")
    Client selectByToken(String token);

    @Update("update client set token = #{token},expire_at = #{expireAt} where id = #{id} ")
    int updateTokenById(@Param("id") String id, @Param("token") String token, @Param("expireAt") long expireAt);

    @Select("select id client_id,token,expire_at from client where token is not null")
    List<ClientToken> getAllToken();
}