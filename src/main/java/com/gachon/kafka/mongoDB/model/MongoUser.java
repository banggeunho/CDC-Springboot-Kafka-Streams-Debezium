package com.gachon.kafka.mongoDB.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;

@Data
//@Table(name = "TB_TARGET")
@Document("target")
@NoArgsConstructor
public class MongoUser {

    @Id
//    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "xxx_SEQUENCE_GENERATOR")
//    @SequenceGenerator(name="xxx_SEQUENCE_GENERATOR", sequenceName = "TEST_SEQ", initialValue = 0, allocationSize = 1)
    private String id;

    @Field("idx")
    private int idx;
    @Field("name")
    private String name;

    @Field("sn")
    private long sn;

    @Field("gender")
    private String gender;

    @Builder
    public MongoUser(String name, int idx, long sn, String gender)
    {
        this.name = name;
        this.idx = idx;
        this.sn = sn;
        this.gender = gender;
    }

    public void update(String name)
    {
        this.name = name;
    }
}
