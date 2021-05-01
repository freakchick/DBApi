package com.jq.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jq.dbapi.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username = #{username} and password = #{password} limit 1")
    User login(String username,String password);

    @Update("update user set password = #{password} where username = 'admin' ")
    void updatePassword(String password);
}
