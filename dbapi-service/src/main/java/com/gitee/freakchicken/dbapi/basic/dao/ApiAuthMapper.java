package com.gitee.freakchicken.dbapi.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiAuth;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ApiAuthMapper extends BaseMapper<ApiAuth> {

    @Delete("delete from api_auth where app_id = #{appId}")
    void deleteByAppId(String appId);

    @Select("select group_id from api_auth where app_id = #{appId} ")
    List<String> selectByAppId(String appId);

    @Delete("delete from api_auth where group_id = #{groupId}")
    void deleteByGroupId(String groupId);
}
