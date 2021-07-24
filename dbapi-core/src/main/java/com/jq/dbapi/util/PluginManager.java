package com.jq.dbapi.util;

import com.jq.dbapi.plugin.CachePlugin;
import com.jq.dbapi.plugin.TransformPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginManager {

    private static Map<String, CachePlugin> cachePlugins = new ConcurrentHashMap<>();
    private static Map<String, TransformPlugin> transformPlugins = new ConcurrentHashMap<>();

    public static CachePlugin getCachePlugin(String className) {
        if (cachePlugins.containsKey(className)) {
            return cachePlugins.get(className);
        } else {
            try {
                Class clazz = Class.forName(className);
                CachePlugin plugin = (CachePlugin) clazz.newInstance();
                cachePlugins.put(className, plugin);
                return cachePlugins.get(className);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static TransformPlugin getTransformPlugin(String className) {
        if (transformPlugins.containsKey(className)) {
            return transformPlugins.get(className);
        } else {
            try {
                Class clazz = Class.forName(className);
                TransformPlugin plugin = (TransformPlugin) clazz.newInstance();
                transformPlugins.put(className, plugin);
                return transformPlugins.get(className);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
