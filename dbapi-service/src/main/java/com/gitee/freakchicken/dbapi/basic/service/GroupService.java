package com.gitee.freakchicken.dbapi.basic.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gitee.freakchicken.dbapi.basic.dao.ClientAuthMapper;
import com.gitee.freakchicken.dbapi.basic.dao.ApiConfigMapper;
import com.gitee.freakchicken.dbapi.basic.dao.GroupMapper;
import com.gitee.freakchicken.dbapi.basic.domain.Group;
import com.gitee.freakchicken.dbapi.basic.util.UUIDUtil;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@DS("meta-db")
public class GroupService {
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    ApiConfigMapper apiConfigMapper;
    @Autowired
    ClientAuthMapper clientAuthMapper;

    public void insert(Group group) {
        group.setId(UUIDUtil.id());
        group.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        group.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        groupMapper.insert(group);
    }

    @Transactional
    public ResponseDto deleteById(String id) {
        int size = apiConfigMapper.selectCountByGroup(id);
        if (size > 0){
            return ResponseDto.fail("Group is not empty, can not delete");
        }else{
            groupMapper.deleteById(id);
            clientAuthMapper.deleteByGroupId(id);
            return ResponseDto.successWithMsg("Group delete success");
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
