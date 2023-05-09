package com.gitee.freakchicken.dbapi.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ClientAuth;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClientAuthMapper extends BaseMapper<ClientAuth> {

    @Delete("delete from client_auth where client_id = #{clientId}")
    void deleteByClientId(String clientId);

    @Select("select group_id from client_auth where client_id = #{clientId} ")
    List<String> selectByClientId(String clientId);

    @Delete("delete from client_auth where group_id = #{groupId}")
    int deleteByGroupId(String groupId);
}
