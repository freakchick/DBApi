package com.gitee.freakchicken.dbapi.basic.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.gitee.freakchicken.dbapi.basic.dao.AccessLogMapper;
import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@DS("access-log-db")
public class AccessLogService {

    @Autowired
    private AccessLogMapper accessLogMapper;

    @Transactional
    public void insert(AccessLog log) {
        accessLogMapper.insert(log);
    }

    public List<AccessLog> getAll() {
        return accessLogMapper.selectList(null);
    }

    public List<AccessLog> search(String path, String apiId, String appId, long startTime, long endTime, int status) {
        return null;
    }

    public JSONArray countByDay(long start, long end) {
        List<String> dateList = getDateList(start, end);
        List<JSONObject> maps = accessLogMapper.countByDate(start, end);
        JSONObject jo = new JSONObject();
        maps.stream().forEach(t -> {
            jo.put(t.getString("date"), t);
        });
        JSONArray array = new JSONArray();
        dateList.forEach(t -> {
            JSONObject o = jo.getJSONObject(t);
            if (o == null) {
                JSONObject temp = new JSONObject();
                temp.put("date", t);
                temp.put("successNum", 0);
                temp.put("failNum", 0);
                array.add(temp);
            } else {
                array.add(o);
            }
        });

        return array;
    }

    public List<JSONObject> top5api( long start,  long end){
        return accessLogMapper.top5api(start, end);
    }

    public List<JSONObject> top5app(long start, long end){
        return accessLogMapper.top5app(start, end);
    }
    public List<JSONObject> top5duration(long start, long end){
        return accessLogMapper.top5duration(start, end);
    }
    public JSONObject successRatio(long start, long end){
        return accessLogMapper.successRatio(start, end);
    }

    public List<String> getDateList(Long start, Long end) {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        long oneDay = 24 * 60 * 60l;
        List<String> list = new ArrayList<>();
        for (long temp = start; temp <= end; temp += oneDay) {
            list.add(format.format(new Date(temp * 1000)));
        }

        return list;

    }
}
