package com.gitee.freakchicken.dbapi.basic.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.gitee.freakchicken.dbapi.basic.dao.AlarmMapper;
import com.gitee.freakchicken.dbapi.basic.domain.ApiAlarm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-19 17:27
 **/
@Slf4j
@Service
@DS("meta-db")
public class AlarmService {

    @Autowired
    AlarmMapper alarmMapper;

    public List<ApiAlarm> selectByApiId(String apiId){
        return alarmMapper.selectByApiId(apiId);
    }

}