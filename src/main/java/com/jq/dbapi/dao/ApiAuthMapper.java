package com.jq.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jq.dbapi.domain.ApiAuth;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ApiAuthMapper extends BaseMapper<ApiAuth> {

    @Delete("delete from api_auth where token_id = #{tokenId}")
    void deleteByTokenId(Integer tokenId);

    @Select("select group_id from api_auth where token_id = #{tokenId} ")
    List<Integer> selectByTokenId(Integer tokenId);

    @Delete("delete from api_auth where group_id = #{groupId}")
    void deleteByGroupId(Integer groupId);
}
