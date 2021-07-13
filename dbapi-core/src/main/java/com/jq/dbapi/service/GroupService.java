package com.jq.dbapi.service;

import com.jq.dbapi.common.ResponseDto;
import com.jq.dbapi.dao.ApiAuthMapper;
import com.jq.dbapi.dao.ApiConfigMapper;
import com.jq.dbapi.dao.GroupMapper;
import com.jq.dbapi.domain.Group;
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
        groupMapper.insert(group);
    }

    @Transactional
    public ResponseDto deleteById(Integer id) {
        int size = apiConfigMapper.selectCountByGroup(id);
        if (size > 0){
            return ResponseDto.fail("该分组下还有API，不可删除！");
        }else{
            groupMapper.deleteById(id);
            apiAuthMapper.deleteByGroupId(id);
            return ResponseDto.success("删除成功");
        }

    }

    public List<Group> getAll() {
        return groupMapper.selectList(null);
    }
}
