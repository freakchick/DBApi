package com.gitee.freakchicken.dbapi.basic.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

    private static Properties p = new Properties();

    static {

        InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");
        try {
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getKey(String key) {
        return p.getProperty(key);
    }

}