package com.gitee.freakchicken.dbapi.basic.util;

import com.gitee.freakchicken.dbapi.basic.domain.User;

public class ThreadContainer {

    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static Integer getCurrentThreadUserId() {
        User user = userThreadLocal.get();
        return user == null ? null : user.getId();
    }

    public static void setCurrentThreadUser(User user) {
        userThreadLocal.set(user);
    }

    // 防止ThreadLocal内存溢出
    public static void clearCurrentThreadUser() {
        userThreadLocal.remove();
    }
}
