package com.gitee.freakchicken.dbapi.controller;

import com.gitee.freakchicken.dbapi.domain.Group;
import com.gitee.freakchicken.dbapi.service.GroupService;
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
        groupService.insert(group);
    }

    @RequestMapping("/delete/{id}")
    public ResponseDto delete(@PathVariable Integer id) {
        return groupService.deleteById(id);
    }

    @RequestMapping("/getAll")
    public List<Group> getAll() {
        List<Group> list = groupService.getAll();
        return list;
    }
}
