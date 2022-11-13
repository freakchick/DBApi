package com.gitee.freakchicken.dbapi.basic.log;

import com.alibaba.fastjson.JSON;
import com.gitee.freakchicken.dbapi.basic.dao.AccessLogMapper;
import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Properties;

@MapperScan("com.gitee.freakchicken.dbapi.basic.dao")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class AccessLogKafkaReader {

    private static String group;

    public static void main(String[] args) {
        group = args[0];
        new SpringApplicationBuilder(AccessLogKafkaReader.class).web(WebApplicationType.NONE).bannerMode(Banner.Mode.OFF).run(args);
    }

    @Autowired
    AccessLogMapper accessLogMapper;

    @Value("${access.log.kafka.topic}")
    String topic;

    @Value("${spring.kafka.bootstrap-servers}")
    String kafka;

    @PostConstruct
    public void run() {

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(topic));

        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(100);

            for (ConsumerRecord record : consumerRecords) {
                AccessLog accessLog = JSON.parseObject(record.value().toString(), AccessLog.class);
                accessLogMapper.insert(accessLog);
            }
        }

    }
}
