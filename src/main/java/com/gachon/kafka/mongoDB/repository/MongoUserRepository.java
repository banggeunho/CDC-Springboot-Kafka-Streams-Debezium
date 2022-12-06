package com.gachon.kafka.mongoDB.repository;

import com.gachon.kafka.mongoDB.model.MongoUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoUserRepository extends MongoRepository<MongoUser, String> {
    public void deleteByIdx(int idx);

    @Query(" {'idx': ?0} ")
    public Optional<MongoUser> findByIdx(int idx);

//    @Query(" {'idx': ?0} ")
//    public void deleteByIdx(int idx);

}
