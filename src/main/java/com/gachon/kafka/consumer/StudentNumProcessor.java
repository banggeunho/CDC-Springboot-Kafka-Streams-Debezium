package com.gachon.kafka.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.gachon.kafka.srcDB.model.User;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static org.apache.kafka.common.serialization.Serdes.Long;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.Collections;
import io.debezium.serde.DebeziumSerdes;

@Component
public class StudentNumProcessor {
    final Serde<String> STRING_SERDE = Serdes.String();
    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {

        Serde<User> userSerde = DebeziumSerdes.payloadJson(User.class);
        userSerde.configure(Collections.singletonMap("from.field", "after"), false);

        KStream<String, User> stream = streamsBuilder.stream(
                "debezium-connector.src_db.user",
                Consumed.with(Serdes.String(), userSerde));

        KStream<String, User> filter = stream.filter(new Predicate<String, User>() {
            @Override
            public boolean test(String key, User value) { //16부터 19학번
                return value.getSn() > 201600000 && value.getSn() < 201999999;
            }
        });

        filter.peek((key, value) -> System.out.println("Inside filter " + value.getName()));
        filter.to("user-sn-filter");
    }
}
