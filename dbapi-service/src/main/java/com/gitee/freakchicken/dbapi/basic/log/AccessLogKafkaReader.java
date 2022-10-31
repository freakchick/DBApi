package com.gitee.freakchicken.dbapi.basic.log;

import com.alibaba.fastjson.JSON;
import com.gitee.freakchicken.dbapi.basic.dao.AccessLogMapper;
import com.gitee.freakchicken.dbapi.basic.domain.AccessLog;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collections;
import java.util.Properties;


@ComponentScan(value = "com.gitee.freakchicken.dbapi.basic.dao")
public class AccessLogKafkaReader implements ApplicationRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AccessLogKafkaReader.class).web(WebApplicationType.NONE).bannerMode(Banner.Mode.OFF).run(args);
    }

    @Autowired
    AccessLogMapper accessLogMapper;

    @Value("${access.log.kafka.topic}")
    String topic;

    @Value("${spring.kafka.bootstrap-servers}")
    String kafka;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        //1.创建消费者配置信息
        Properties properties = new Properties();
        //链接的集群
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka);
        //开启自动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        //自动提交的延迟
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        //key,value的反序列化
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        //消费者组
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "dbapi-etl-group");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");//重置消费者offset的方法（达到重复消费的目的），设置该属性也只在两种情况下生效：1.上面设置的消费组还未消费(可以更改组名来消费)2.该offset已经过期

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
