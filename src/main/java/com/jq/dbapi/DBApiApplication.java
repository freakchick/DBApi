package com.jq.dbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class DBApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DBApiApplication.class, args);
    }
}
