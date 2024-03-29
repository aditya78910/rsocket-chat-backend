package com.yahoo.chat.repository.mongo.userchats;

import com.yahoo.chat.model.mongo.UserChats;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserChatsRepository extends ReactiveMongoRepository<UserChats, String>, UserChatsRepositoryCustom {
}
