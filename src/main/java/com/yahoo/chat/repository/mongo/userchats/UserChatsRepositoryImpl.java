package com.yahoo.chat.repository.mongo.userchats;

import com.mongodb.client.result.UpdateResult;
import com.yahoo.chat.model.mongo.UserChats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;

@Slf4j
public class UserChatsRepositoryImpl implements UserChatsRepositoryCustom {

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;


    public Flux<UpdateResult> updateUserChat(Set<String> userIds, UUID chatRoomId){

        return Flux.fromIterable(userIds)
                .flatMap(userId -> addChatroomToUser(userId, chatRoomId));

    }


    public Mono<UpdateResult> addChatroomToUser(String userId, UUID chatRoomId){
        Query query = new Query(Criteria.where("_id").is(userId));


        // Perform an upsert operation
        Update update = new Update()
                .push("chatRooms", chatRoomId)
                .set("_id", userId);
        return reactiveMongoTemplate.upsert(query, update, UserChats.class);
    }
}
