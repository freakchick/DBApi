package com.gitee.freakchicken.dbapi.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiDto;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ApiConfigMapper extends BaseMapper<ApiConfig> {

    @Select("select * from api_config where path=#{path} and status = 1")
    ApiConfig selectByPathOnline(String path);

    @Select("<script>" +
            "select * from api_config\n" +
            "<where>\n" +
            "<if test='groupId != null and groupId !=\"\"'> group_id = #{groupId} </if>\n" +
            "<if test='keyword != null and keyword !=\"\"'>\n" +
            "\t<if test='field != null and field !=\"\"'> and ${field} like #{keyword} </if>\n" +
            "\t<if test='field == null or field ==\"\"'> and (name like #{keyword} or note like #{keyword} or path like #{keyword} )</if>\n" +
            "</if>\n" +
            "</where>" +
            "</script>")
    List<ApiConfig> selectByKeyword(@Param("keyword") String keyword, @Param("field") String field, @Param("groupId") String groupId);

    @Select("select count(1) from api_config where path=#{path}")
    Integer selectCountByPath(String path);

    @Select("select count(1) from api_config where path=#{path} and id != #{id}")
    Integer selectCountByPathWhenUpdate(@Param("path") String path, @Param("id") String id);

//     @Select("select count(1) from api_config where datasource_id = #{id}")
//     int countByDatasoure(String id);

    @Select("select count(1) from api_config where group_id = #{id}")
    int selectCountByGroup(String id);

    @Results(id = "accResultMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "group_name", column = "groupName")
    })
    @Select("select t1.id,t1.name,t2.name as group_name from api_config t1 join api_group t2 on t1.group_id = t2.id")
    List<ApiDto> getAllDetail();
}
