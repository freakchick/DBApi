package com.gitee.freakchicken.dbapi.plugin;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class PluginManager {

    private static Map<String, BasePlugin> cachePluginsMap = new ConcurrentHashMap<>();
    private static Map<String, BasePlugin> transformPluginsMap = new ConcurrentHashMap<>();
    private static Map<String, BasePlugin> globalTransformPluginsMap = new ConcurrentHashMap<>();
    private static Map<String, BasePlugin> alarmPluginsMap = new ConcurrentHashMap<>();

    private static List<JSONObject> allCachePlugins = new ArrayList<>();
    private static List<JSONObject> allTransformPlugins = new ArrayList<>();
    private static List<JSONObject> allGlobalTransformPlugins = new ArrayList<>();
    private static List<JSONObject> allAlarmPlugins = new ArrayList<>();

    public static void loadPlugins() {

        ServiceLoader<CachePlugin> serviceLoader = ServiceLoader.load(CachePlugin.class);
        Iterator<CachePlugin> CachePlugins = serviceLoader.iterator();
        while (CachePlugins.hasNext()) {
            CachePlugin plugin = CachePlugins.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            cachePluginsMap.put(plugin.getClass().getName(), plugin);
        }
        allCachePlugins = getAllList(cachePluginsMap);
        log.info("scan cache plugin finish");

        ServiceLoader<TransformPlugin> serviceLoader2 = ServiceLoader.load(TransformPlugin.class);
        Iterator<TransformPlugin> TransformPlugins = serviceLoader2.iterator();
        while (TransformPlugins.hasNext()) {
            TransformPlugin plugin = TransformPlugins.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            transformPluginsMap.put(plugin.getClass().getName(), plugin);
        }
        allTransformPlugins = getAllList(transformPluginsMap);
        log.info("scan transform plugin finish");

        ServiceLoader<AlarmPlugin> serviceLoader3 = ServiceLoader.load(AlarmPlugin.class);
        Iterator<AlarmPlugin> AlarmPlugins = serviceLoader3.iterator();
        while (AlarmPlugins.hasNext()) {
            AlarmPlugin plugin = AlarmPlugins.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            alarmPluginsMap.put(plugin.getClass().getName(), plugin);
        }
        allAlarmPlugins = getAllList(alarmPluginsMap);
        log.info("scan alarm plugin finish");

        ServiceLoader<GlobalTransformPlugin> serviceLoader4 = ServiceLoader.load(GlobalTransformPlugin.class);
        Iterator<GlobalTransformPlugin> GlobalTransformPlugins = serviceLoader4.iterator();
        while (GlobalTransformPlugins.hasNext()) {
            GlobalTransformPlugin plugin = GlobalTransformPlugins.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            globalTransformPluginsMap.put(plugin.getClass().getName(), plugin);
        }
        allGlobalTransformPlugins = getAllList(globalTransformPluginsMap);
        log.info("scan global transform plugin finish");
    }

    public static CachePlugin getCachePlugin(String className) {
        if (!cachePluginsMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (CachePlugin) cachePluginsMap.get(className);
    }

    public static TransformPlugin getTransformPlugin(String className) {
        if (!transformPluginsMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (TransformPlugin)transformPluginsMap.get(className);
    }

    public static GlobalTransformPlugin getGlobalTransformPlugin(String className) {
        if (!globalTransformPluginsMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (GlobalTransformPlugin)globalTransformPluginsMap.get(className);
    }

    public static AlarmPlugin getAlarmPlugin(String className) {
        if (!alarmPluginsMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (AlarmPlugin)alarmPluginsMap.get(className);
    }

    private static List<JSONObject> getAllList(Map<String, BasePlugin> map) {
        List<JSONObject> collect = map.values().stream().map(t -> {
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
