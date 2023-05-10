package com.gitee.freakchicken.dbapi.basic.util;

public class Constants {

    public static String EHCACHE_CLIENT_TOKEN = "client_token";
    public static String EHCACHE_TOKEN_CLIENT = "token_client";
    public static String EHCACHE_CLIENT_AUTH_GROUPS = "client_auth_groups";

    public static int API_STATUS_ONLINE = 1;
    public static int API_STATUS_OFFLINE = 0;

    public static int API_ACCESS_PUBLIC = 1;
    public static int API_ACCESS_PRIVATE = 0;

    public static int API_EXECUTOR_SQL = 1;
    public static int API_EXECUTOR_ES = 2;
    public static int API_EXECUTOR_HTTP = 3;

    public static int PLUGIN_TYPE_CACHE = 1;
    public static int PLUGIN_TYPE_ALARM = 2;
    public static int PLUGIN_TYPE_GLOBAL_TRANSFORM = 3;
    public static int PLUGIN_TYPE_TRANSFORM = 4;

}
