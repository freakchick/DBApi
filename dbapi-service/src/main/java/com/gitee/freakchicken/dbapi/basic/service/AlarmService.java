package com.gitee.freakchicken.dbapi.basic.service;

import com.gitee.freakchicken.dbapi.basic.dao.AlarmMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-19 17:27
 **/
@Slf4j
@Service
public class AlarmService {

    @Autowired
    AlarmMapper alarmMapper;

    public String getMail(String apiId){
        String mail = alarmMapper.selectMailByApiId(apiId);
        return mail;
    }

}