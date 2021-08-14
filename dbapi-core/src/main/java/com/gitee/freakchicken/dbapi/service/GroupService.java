package com.gitee.freakchicken.dbapi.service;

import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.dao.GroupMapper;
import com.gitee.freakchicken.dbapi.dao.ApiAuthMapper;
import com.gitee.freakchicken.dbapi.dao.ApiConfigMapper;
import com.gitee.freakchicken.dbapi.domain.Group;
import com.gitee.freakchicken.dbapi.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    ApiConfigMapper apiConfigMapper;
    @Autowired
    ApiAuthMapper apiAuthMapper;

    public void insert(Group group) {
        group.setId(UUIDUtil.id());
        groupMapper.insert(group);
    }

    @Transactional
    public ResponseDto deleteById(String id) {
        int size = apiConfigMapper.selectCountByGroup(id);
        if (size > 0){
            return ResponseDto.fail("该分组下还有API，不可删除！");
        }else{
            groupMapper.deleteById(id);
            apiAuthMapper.deleteByGroupId(id);
            return ResponseDto.successWithMsg("删除成功");
        }

    }

    public List<Group> getAll() {
        return groupMapper.selectList(null);
    }
}
