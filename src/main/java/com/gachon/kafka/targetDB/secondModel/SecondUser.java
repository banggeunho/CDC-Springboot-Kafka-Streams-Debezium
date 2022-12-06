package com.gachon.kafka.targetDB.secondModel;

import com.gachon.kafka.srcDB.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@Entity
public class SecondUser {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;

    @Column(name = "sn")
    private long sn;

    @Column(name="gender")
    private String gender;

    @Builder
    public SecondUser(String name, long sn, String gender){
        this.name = name;
        this.sn =sn;
        this.gender = gender;
    }
}
