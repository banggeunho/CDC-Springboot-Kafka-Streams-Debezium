package com.gachon.kafka.srcDB.repository;

import com.gachon.kafka.srcDB.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

//    @Query(" {'delYn': 'N', 'email': ?0} ")
//    public User findById(Integer id);
//
//    @Query(" select s from Surveys s where s.delYn = 'N' and s.email = ?1 and s.svyType= ?2")
}
