package com.gitee.freakchicken.dbapi.util;

import java.util.UUID;

public class UUIDUtil {

    public static String id() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
