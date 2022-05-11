package com.gitee.freakchicken.dbapi.basic.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadUtils {

    private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("alarm thread pool").build();
    private static ExecutorService executorService = Executors.newFixedThreadPool(1, threadFactory);

    public static void submitAlarmTask(Runnable task) {
        executorService.submit(task);
    }
}
