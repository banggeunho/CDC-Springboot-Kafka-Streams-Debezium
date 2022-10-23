package com.gachon.kafka.targetDB.secondRepository;


import com.gachon.kafka.srcDB.model.User;
//import com.gachon.kafka.targetDB.secondModel.SecondUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondUserRepository extends JpaRepository<User, Integer> {
}
