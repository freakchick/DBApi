package com.gitee.freakchicken.dbapi.basic.util;

import com.gitee.freakchicken.dbapi.basic.domain.User;

public class ThreadContainer {

    public static ThreadLocal<String> clientThreadLocal = new ThreadLocal<>();

    public static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();



}
