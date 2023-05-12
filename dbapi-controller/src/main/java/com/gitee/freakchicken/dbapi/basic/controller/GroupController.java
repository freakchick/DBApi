package com.gitee.freakchicken.dbapi.basic.controller;

import com.gitee.freakchicken.dbapi.basic.domain.Group;
import com.gitee.freakchicken.dbapi.basic.service.GroupService;
import com.gitee.freakchicken.dbapi.basic.util.ThreadContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    GroupService groupService;

    @RequestMapping("/create")
    public void create(Group group) {
        group.setCreateUserId(ThreadContainer.getCurrentThreadUserId());
        groupService.insert(group);
    }

    @RequestMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable String id) {
        return groupService.deleteById(id);
    }

    @RequestMapping("/getAll")
    public List<Group> getAll() {
        List<Group> list = groupService.getAll();
        return list;
    }
}
