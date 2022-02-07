package com.gitee.freakchicken.dbapi.basic.service;

import com.gitee.freakchicken.dbapi.basic.dao.IPMapper;
import com.gitee.freakchicken.dbapi.basic.util.IPRuleCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IPService {


    @Autowired
    private IPMapper ipMapper;
    @Autowired
    private MetaDataCacheManager metaDataCacheManager;

    /**
     * @param mode
     * @param ip   字符串
     */
    public void on(String mode, String ip) {
        _on(mode, ip);

        // 设置缓存
        init();
        metaDataCacheManager.gatewayIPRuleCacheSyncIfCluster();

    }

    @Transactional
    public void _on(String mode, String ip) {
        ipMapper.turnOn(mode);
        ipMapper.saveIP(ip, mode);
    }

    public void off() {
        _off();
        // 设置缓存
        init();
        metaDataCacheManager.gatewayIPRuleCacheSyncIfCluster();
    }

    @Transactional
    public void _off() {
        ipMapper.turnOff();
    }


    @PostConstruct
    public void init() {
        log.info("init ip service...");
        String blackIP = ipMapper.getBlackIP();
        String whiteIP = ipMapper.getWhiteIP();
        if (StringUtils.isNoneBlank(blackIP)) {
            IPRuleCache.blackIPSet = Arrays.asList(blackIP.split("\n")).stream().map(t -> t.trim())
                    .filter(t -> StringUtils.isNoneBlank(t)).collect(Collectors.toSet());
        } else {
            IPRuleCache.blackIPSet = new HashSet<>();
        }
        if (StringUtils.isNoneBlank(whiteIP)) {
            IPRuleCache.whiteIPSet = Arrays.asList(whiteIP.split("\n")).stream().map(t -> t.trim())
                    .filter(t -> StringUtils.isNoneBlank(t)).collect(Collectors.toSet());
        } else {
            IPRuleCache.whiteIPSet = new HashSet<>();
        }
        Map<String, String> map = ipMapper.getStatus();
        IPRuleCache.mode = map.get("mode");
        IPRuleCache.status = map.get("status");
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

    public boolean checkIP(String originIp) {
        if (IPRuleCache.status.equals("on")) {
            if (IPRuleCache.mode.equals("black")) {
                if (IPRuleCache.blackIPSet.contains(originIp)) {
                    return false;
                } else {
                    return true;
                }
            } else if (IPRuleCache.mode.equals("white")) {
                if (IPRuleCache.whiteIPSet.contains(originIp)) {
                    return true;
                } else {
                    return false;
                }
            }

        }
        return true;
    }

}
