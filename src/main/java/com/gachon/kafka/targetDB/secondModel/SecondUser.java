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

    @Builder
    public SecondUser(String name){
        this.name = name;
    }
}
