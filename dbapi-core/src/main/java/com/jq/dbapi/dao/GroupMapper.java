package com.jq.dbapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jq.dbapi.domain.Group;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GroupMapper extends BaseMapper<Group> {
}
