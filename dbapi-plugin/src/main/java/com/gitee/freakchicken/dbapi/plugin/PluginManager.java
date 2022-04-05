package com.gitee.freakchicken.dbapi.plugin;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PluginManager {

    private static Map<String, CachePlugin> cachePlugins = new ConcurrentHashMap<>();
    private static Map<String, TransformPlugin> transformPlugins = new ConcurrentHashMap<>();

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
    }

    public static CachePlugin getCachePlugin(String className) {
        return cachePlugins.get(className);
    }

    public static TransformPlugin getTransformPlugin(String className) {
        return transformPlugins.get(className);
    }

    public static Set<String> getAllCachePlugin() {
        return cachePlugins.keySet();
    }

    public static Set<String> getAllTransformPlugin() {
        return transformPlugins.keySet();
    }
}
