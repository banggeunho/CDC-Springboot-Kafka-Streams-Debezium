package com.gachon.kafka.srcDB.repository;

import com.gachon.kafka.srcDB.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
