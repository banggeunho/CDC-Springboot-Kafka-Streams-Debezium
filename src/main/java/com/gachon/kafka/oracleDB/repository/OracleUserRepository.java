package com.gachon.kafka.oracleDB.repository;

import com.gachon.kafka.oracleDB.model.OracleUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OracleUserRepository extends JpaRepository<OracleUser, Integer> {
}