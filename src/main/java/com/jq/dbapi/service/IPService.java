package com.jq.dbapi.service;

import com.jq.dbapi.dao.IPMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IPService {

    @Autowired
    IPMapper ipMapper;

    @Transactional
    public void on(String mode, String ip) {
        ipMapper.turnOn(mode);
        ipMapper.saveIP(ip, mode);
    }

    @Transactional
    public void off() {
        ipMapper.turnoff();
    }

    public Map<String, String> detail() {
        List<Map<String, String>> ipRule = ipMapper.getIPRule();
        Map<String, String> status = ipMapper.getStatus();
        ipRule.stream().forEach(t -> {
            String type = t.get("type");
            String ip = t.get("ip");
            if (type.equals("white")) {
                status.put("whiteIP", ip);
            } else if (type.equals("black")) {
                status.put("blackIP", ip);
            }
        });
        return status;
    }

    public boolean check(String mode, String ipList, String originIp) {
        String[] items = ipList.split("\n");
        Set<String> set = Arrays.asList(items).stream().map(t -> t.trim()).collect(Collectors.toSet());

        if (mode.equals("black")) {
            if (set.contains(originIp)) {
                return false;
            } else
                return true;
        } else if (mode.equals("white")) {
            if (set.contains(originIp)) {
                return true;
            } else
                return false;
        }
        return false;
    }
}
