package com.gachon.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.gachon.kafka.mongoDB")
public class DebeziumApplication {

    public static void main(String[] args) {
        SpringApplication.run(DebeziumApplication.class, args);
    }

}
