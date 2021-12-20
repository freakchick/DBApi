package com.gitee.freakchicken.dbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("application-gateway.yml")
public class DBApiGateWay {
    public static void main(String[] args) {
        SpringApplication.run(DBApiGateWay.class, args);
    }

}
