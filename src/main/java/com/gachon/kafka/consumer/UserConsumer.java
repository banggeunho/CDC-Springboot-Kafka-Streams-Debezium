package com.gachon.kafka.consumer;

import com.gachon.kafka.mongoDB.model.MongoUser;
import com.gachon.kafka.mongoDB.repository.MongoUserRepository;
import com.gachon.kafka.oracleDB.model.OracleUser;
//import com.gachon.kafka.oracleDB.repository.OracleUserRepository;
import com.gachon.kafka.srcDB.model.User;
import com.gachon.kafka.targetDB.secondModel.SecondUser;
import com.gachon.kafka.targetDB.secondRepository.SecondUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.DeleteResult;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.weaver.ast.Or;


import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.crypto.SealedObject;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
//@AllArgsConstructor
//@NoArgsConstructor
public class UserConsumer {

    private final ObjectMapper mapper;
    private final SecondUserRepository secondUserRepository;
    private final MongoUserRepository mongoUserRepository;

    @KafkaListener(topics = "debezium-connector.src_db.user")
    public void consumeUser(ConsumerRecord<String, String> record) throws JsonProcessingException {
        String consumedValue = record.value();

        var jsonNode = mapper.readTree(consumedValue);
        JsonNode payload = jsonNode.path("payload");
        String op = payload.get("op").toString().substring(1,2);
        System.out.println(payload);
//        System.out.println(after.get("name").toString());
//        System.out.println(op.substring(1));


        switch (op) {
            case "c": {
                JsonNode after = payload.path("after");
                User mysqlUser = User.builder()
                        .name(after.get("name").toString())
                        .sn(after.get("sn").longValue())
                        .gender(after.get("gender").toString())
                        .build();
//
                MongoUser mongoUser = MongoUser.builder()
                        .name(after.get("name").toString())
                        .idx(Integer.parseInt(after.get("id").toString()))
                        .sn(after.get("sn").longValue())
                        .gender(after.get("gender").toString())
                        .build();

                secondUserRepository.save(mysqlUser);
                mongoUserRepository.save(mongoUser);
                break;
            }
            case "u": {
                JsonNode after = payload.path("after");
                User mysqlUser = secondUserRepository.findById(Integer.parseInt(after.get("id").toString()))
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id입니다. " + after.get("id").toString()));

                MongoUser mongoUser = mongoUserRepository.findByIdx(Integer.parseInt(after.get("id").toString()))
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id입니다. " + after.get("id").toString()));

                mysqlUser.update(after.get("name").toString());
                mongoUser.update(after.get("name").toString());

                secondUserRepository.save(mysqlUser);
                mongoUserRepository.save(mongoUser);
                break;
            }
            case "d": {
                JsonNode before = payload.path("before");
                System.out.println(before.get("id").toString());
//            User mysqlUser = User.builder()
//                    .name(before.get("id").toString())
//                    .build();
//
                User mysqlUser = secondUserRepository.findById(Integer.parseInt(before.get("id").toString()))
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id입니다. " + before.get("id").toString()));
//
                MongoUser mongoUser = mongoUserRepository.findByIdx(Integer.parseInt(before.get("id").toString()))
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 id입니다. " + before.get("id").toString()));
//
                System.out.println(mysqlUser.toString());
                System.out.println(mongoUser.toString());
                System.out.println(mysqlUser.getId());
                System.out.println(mongoUser.getId());
//            Query query = new Query(Criteria.where("idx").is(before.get("id").toString()))

                secondUserRepository.deleteById(mysqlUser.getId());
                mongoUserRepository.deleteById(mongoUser.getId());
                break;
            }
        }
    }

//    @KafkaListener(topics = "dbserver2.LOGMINER.TB_SRC")
//    public void consumeOracleUser(ConsumerRecord<String, String> record) throws JsonProcessingException {
//        String consumedValue = record.value();
//
//        var jsonNode = mapper.readTree(consumedValue);
//        JsonNode payload = jsonNode.path("payload");
//        JsonNode after = payload.path("after");
//        System.out.println(payload);
//
//
////        String userString = after.toString();
////        OracleUser user = mapper.readValue(userString, OracleUser.class);
//
//        System.out.println(after.get("name").toString());
//
//        User mysqlUser = User.builder()
//                .name(after.get("name").toString())
//                .build();
////
//        OracleUser oracleUser = OracleUser.builder()
//                .name(after.get("name").toString())
//                .build();
////
//        secondUserRepository.save(mysqlUser);
//        oracleUserRepository.save(oracleUser);
//    }

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
