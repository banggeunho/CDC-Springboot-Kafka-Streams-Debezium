package com.gachon.kafka.consumer;

import com.gachon.kafka.oracleDB.model.OracleUser;
import com.gachon.kafka.oracleDB.repository.OracleUserRepository;
import com.gachon.kafka.srcDB.model.User;
import com.gachon.kafka.targetDB.secondModel.SecondUser;
import com.gachon.kafka.targetDB.secondRepository.SecondUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.weaver.ast.Or;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.crypto.SealedObject;

@Component
@RequiredArgsConstructor
public class UserConsumer {

    private final ObjectMapper mapper;
    private final SecondUserRepository secondUserRepository;
    private final OracleUserRepository oracleUserRepository;

    @KafkaListener(topics = "dbserver1.src_db.user")
    public void consumeUser(ConsumerRecord<String, String> record) throws JsonProcessingException {
        String consumedValue = record.value();

        var jsonNode = mapper.readTree(consumedValue);
        JsonNode payload = jsonNode.path("payload");
        JsonNode after = payload.path("after");
        System.out.println(payload);


        System.out.println(after.get("name").toString());

        User mysqlUser = User.builder()
                .name(after.get("name").toString())
                .build();
//
        OracleUser oracleUser = OracleUser.builder()
                .name(after.get("name").toString())
                .build();
//
        secondUserRepository.save(mysqlUser);
        oracleUserRepository.save(oracleUser);
    }

    @KafkaListener(topics = "dbserver2.LOGMINER.TB_SRC")
    public void consumeOracleUser(ConsumerRecord<String, String> record) throws JsonProcessingException {
        String consumedValue = record.value();

        var jsonNode = mapper.readTree(consumedValue);
        JsonNode payload = jsonNode.path("payload");
        JsonNode after = payload.path("after");
        System.out.println(payload);


//        String userString = after.toString();
//        OracleUser user = mapper.readValue(userString, OracleUser.class);

        System.out.println(after.get("name").toString());

        User mysqlUser = User.builder()
                .name(after.get("name").toString())
                .build();
//
        OracleUser oracleUser = OracleUser.builder()
                .name(after.get("name").toString())
                .build();
//
        secondUserRepository.save(mysqlUser);
        oracleUserRepository.save(oracleUser);
    }

//    @KafkaListener(topics = "dbserver1.db.user")
//    public void consumeUser(ConsumerRecord<String, String> record) throws JsonProcessingException {
//        String consumedValue = record.value();
//
//        var jsonNode = mapper.readTree(consumedValue);
//        JsonNode payload = jsonNode.path("payload");
//        JsonNode after = payload.path("after");
//
//        String userString = after.toString();
//        User user = mapper.readValue(userString, User.class);
//
//        MongoUser mongoUser = MongoUser.builder()
//                .user(user)
//                .build();
//
//        userMongoRepository.save(mongoUser);
//    }
}
