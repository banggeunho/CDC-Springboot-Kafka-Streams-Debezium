package com.gachon.kafka.oracleDB.model;

import com.gachon.kafka.srcDB.model.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "TB_TARGET")
@NoArgsConstructor
public class OracleUser {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "xxx_SEQUENCE_GENERATOR")
    @SequenceGenerator(name="xxx_SEQUENCE_GENERATOR", sequenceName = "TEST_SEQ", initialValue = 0, allocationSize = 1)
    private int id;

    @Column(name="name")
    private String name;

    @Builder
    public OracleUser(String name){
        this.name = name;
    }
}
