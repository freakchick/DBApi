package com.gitee.freakchicken.dbapi.plugin;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class PluginManager {

    private static Map<String, BasePlugin> cachePluginMap = new ConcurrentHashMap<>();
    private static Map<String, BasePlugin> transformPluginMap = new ConcurrentHashMap<>();
    private static Map<String, BasePlugin> globalTransformPluginMap = new ConcurrentHashMap<>();
    private static Map<String, BasePlugin> alarmPluginMap = new ConcurrentHashMap<>();
    private static Map<String, BasePlugin> parameterValidatorMap = new ConcurrentHashMap<>();

    private static List<JSONObject> allCachePlugins = new ArrayList<>();
    private static List<JSONObject> allTransformPlugins = new ArrayList<>();
    private static List<JSONObject> allGlobalTransformPlugins = new ArrayList<>();
    private static List<JSONObject> allAlarmPlugins = new ArrayList<>();
    private static List<JSONObject> allParameterValidators = new ArrayList<>();

    public static void loadPlugins() {

        ServiceLoader<CachePlugin> serviceLoader = ServiceLoader.load(CachePlugin.class);
        Iterator<CachePlugin> CachePlugins = serviceLoader.iterator();
        while (CachePlugins.hasNext()) {
            CachePlugin plugin = CachePlugins.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            cachePluginMap.put(plugin.getClass().getName(), plugin);
        }
        allCachePlugins = getAllList(cachePluginMap);
        log.info("scan cache plugin finish");

        ServiceLoader<TransformPlugin> serviceLoader2 = ServiceLoader.load(TransformPlugin.class);
        Iterator<TransformPlugin> TransformPlugins = serviceLoader2.iterator();
        while (TransformPlugins.hasNext()) {
            TransformPlugin plugin = TransformPlugins.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            transformPluginMap.put(plugin.getClass().getName(), plugin);
        }
        allTransformPlugins = getAllList(transformPluginMap);
        log.info("scan transform plugin finish");

        ServiceLoader<AlarmPlugin> serviceLoader3 = ServiceLoader.load(AlarmPlugin.class);
        Iterator<AlarmPlugin> AlarmPlugins = serviceLoader3.iterator();
        while (AlarmPlugins.hasNext()) {
            AlarmPlugin plugin = AlarmPlugins.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            alarmPluginMap.put(plugin.getClass().getName(), plugin);
        }
        allAlarmPlugins = getAllList(alarmPluginMap);
        log.info("scan alarm plugin finish");

        ServiceLoader<GlobalTransformPlugin> serviceLoader4 = ServiceLoader.load(GlobalTransformPlugin.class);
        Iterator<GlobalTransformPlugin> GlobalTransformPlugins = serviceLoader4.iterator();
        while (GlobalTransformPlugins.hasNext()) {
            GlobalTransformPlugin plugin = GlobalTransformPlugins.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            globalTransformPluginMap.put(plugin.getClass().getName(), plugin);
        }
        allGlobalTransformPlugins = getAllList(globalTransformPluginMap);
        log.info("scan global transform plugin finish");

        ServiceLoader<ParameterValidator> serviceLoader5 = ServiceLoader.load(ParameterValidator.class);
        Iterator<ParameterValidator> ParameterValidators = serviceLoader5.iterator();
        while (ParameterValidators.hasNext()) {
            ParameterValidator plugin = ParameterValidators.next();
            plugin.init();
            log.info("{} registered", plugin.getClass().getName());
            parameterValidatorMap.put(plugin.getClass().getName(), plugin);
        }
        allParameterValidators = getAllList(parameterValidatorMap);
        log.info("scan validator plugin finish");
    }

    public static CachePlugin getCachePlugin(String className) {
        if (!cachePluginMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (CachePlugin) cachePluginMap.get(className);
    }

    public static TransformPlugin getTransformPlugin(String className) {
        if (!transformPluginMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (TransformPlugin) transformPluginMap.get(className);
    }

    public static GlobalTransformPlugin getGlobalTransformPlugin(String className) {
        if (!globalTransformPluginMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (GlobalTransformPlugin) globalTransformPluginMap.get(className);
    }

    public static AlarmPlugin getAlarmPlugin(String className) {
        if (!alarmPluginMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (AlarmPlugin) alarmPluginMap.get(className);
    }

    public static ParameterValidator getParameterValidator(String className) {
        if (!parameterValidatorMap.containsKey(className)) {
            throw new RuntimeException("Plugin not found: " + className);
        }
        return (ParameterValidator) parameterValidatorMap.get(className);
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

    public static List<JSONObject> getAllCachePlugins() {
        return allCachePlugins;
    }

    public static List<JSONObject> getAllAlarmPlugins() {
        return allAlarmPlugins;
    }

    public static List<JSONObject> getAllTransformPlugins() {
        return allTransformPlugins;
    }

    public static List<JSONObject> getAllGlobalTransformPlugins() {
        return allGlobalTransformPlugins;
    }

    public static List<JSONObject> getAllParameterValidators() {
        return allParameterValidators;
    }
}
