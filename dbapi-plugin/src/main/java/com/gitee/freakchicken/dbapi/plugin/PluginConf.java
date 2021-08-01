package com.gitee.freakchicken.dbapi.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PluginConf {

    private static Properties p = new Properties();

    static {

        InputStream in = PluginConf.class.getClassLoader().getResourceAsStream("plugin.properties");
        try {
            p.load(in);
        } catch (IOException e) {

        }

    }

    public static String getKey(String key) {
        return p.getProperty(key);
    }

}
