package com.gitee.freakchicken.dbapi.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class UUIDUtil {

    public static String id() {


        return RandomStringUtils.random(8,true,true);
    }

}
