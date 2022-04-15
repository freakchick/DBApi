package com.gitee.freakchicken.dbapi.plugin;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class PluginManager {

    private static Map<String, CachePlugin> cachePlugins = new ConcurrentHashMap<>();
    private static Map<String, TransformPlugin> transformPlugins = new ConcurrentHashMap<>();
    private static Map<String, AlarmPlugin> alarmPlugins = new ConcurrentHashMap<>();

    public static void loadPlugins() {

        ServiceLoader<CachePlugin> serviceLoader = ServiceLoader.load(CachePlugin.class);
        Iterator<CachePlugin> CachePlugins = serviceLoader.iterator();
        while (CachePlugins.hasNext()) {
            CachePlugin plugin = CachePlugins.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            cachePlugins.put(plugin.getClass().getName(), plugin);
        }
        log.info("scan cache plugin finish");

        ServiceLoader<TransformPlugin> serviceLoader2 = ServiceLoader.load(TransformPlugin.class);
        Iterator<TransformPlugin> TransformPlugins = serviceLoader2.iterator();
        while (TransformPlugins.hasNext()) {
            TransformPlugin plugin = TransformPlugins.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            transformPlugins.put(plugin.getClass().getName(), plugin);
        }
        log.info("scan transform plugin finish");

        ServiceLoader<AlarmPlugin> serviceLoader3 = ServiceLoader.load(AlarmPlugin.class);
        Iterator<AlarmPlugin> AlarmPlugins = serviceLoader3.iterator();
        while (AlarmPlugins.hasNext()) {
            AlarmPlugin plugin = AlarmPlugins.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            alarmPlugins.put(plugin.getClass().getName(), plugin);
        }
        log.info("scan alarm plugin finish");
    }

    public static CachePlugin getCachePlugin(String className) {
        return cachePlugins.get(className);
    }

    public static TransformPlugin getTransformPlugin(String className) {
        return transformPlugins.get(className);
    }

    public static AlarmPlugin getAlarmPlugin(String className) {
        return alarmPlugins.get(className);
    }

    public static List<JSONObject> getAllCachePlugin() {
        List<JSONObject> collect = cachePlugins.values().stream().map(t -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("className", t.getClass().getName());
            jsonObject.put("name", t.getName());
            jsonObject.put("description", t.getDescription());
            jsonObject.put("paramDescription", t.getParamDescription());
            return jsonObject;
        }).collect(Collectors.toList());
        return collect;
    }

    public static List<JSONObject> getAllTransformPlugin() {
//        return transformPlugins.keySet();
        List<JSONObject> collect = transformPlugins.values().stream().map(t -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("className", t.getClass().getName());
            jsonObject.put("name", t.getName());
            jsonObject.put("description", t.getDescription());
            jsonObject.put("paramDescription", t.getParamDescription());
            return jsonObject;
        }).collect(Collectors.toList());
        return collect;
    }

    public static List<JSONObject> getAllAlarmPlugin() {
//        return alarmPlugins.keySet();
        List<JSONObject> collect = alarmPlugins.values().stream().map(t -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("className", t.getClass().getName());
            jsonObject.put("name", t.getName());
            jsonObject.put("description", t.getDescription());
            jsonObject.put("paramDescription", t.getParamDescription());
            return jsonObject;
        }).collect(Collectors.toList());
        return collect;
    }
}
