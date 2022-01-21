package com.gitee.freakchicken.dbapi.basic.service;

import com.gitee.freakchicken.dbapi.basic.dao.ApiAuthMapper;
import com.gitee.freakchicken.dbapi.basic.dao.ApiConfigMapper;
import com.gitee.freakchicken.dbapi.basic.dao.GroupMapper;
import com.gitee.freakchicken.dbapi.basic.domain.Group;
import com.gitee.freakchicken.dbapi.basic.util.UUIDUtil;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
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
            return ResponseDto.fail("group is not empty, can not delete");
        }else{
            groupMapper.deleteById(id);
            apiAuthMapper.deleteByGroupId(id);
            return ResponseDto.successWithMsg("delete success");
        }

    }

    public List<Group> getAll() {
        return groupMapper.selectList(null);
    }

    public List<Group> selectBatch(List<String> ids) {
        return groupMapper.selectBatchIds(ids);
    }

    @Transactional
    public void insertBatch(List<Group> configs) {
        configs.stream().forEach(t->{
            groupMapper.insert(t);
        });
    }
}
