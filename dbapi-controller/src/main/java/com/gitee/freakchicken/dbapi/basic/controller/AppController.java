package com.gitee.freakchicken.dbapi.basic.controller;

import com.gitee.freakchicken.dbapi.basic.domain.AppInfo;
import com.gitee.freakchicken.dbapi.basic.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private AppService appService;

    /**
     * 创建应用
     *
     * @param app
     * @return
     */
    @PostMapping("/create")
    public AppInfo createApp(AppInfo app) {
        AppInfo add = appService.add(app);
        return add;
    }

    @PostMapping("/getAll")
    public List<AppInfo> getAll() {
        List<AppInfo> list = appService.getAll();
        return list;
    }

    @PostMapping("/delete/{appid}")
    public void delete(@PathVariable("appid") String appid) {
        appService.delete(appid);
    }

    @PostMapping("/auth")
    public void auth(String appId, String groupIds) {
        appService.auth(appId, groupIds);
    }

    @PostMapping("/getAuthGroups/{appid}")
    public List<String> getAuthGroups(@PathVariable("appid") String appId) {
        return appService.getAuthGroups(appId);
    }
}