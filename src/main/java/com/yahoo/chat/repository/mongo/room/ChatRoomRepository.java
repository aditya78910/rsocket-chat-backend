package com.yahoo.chat.repository.mongo.room;

import com.yahoo.chat.model.mongo.ChatRoom;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


public interface ChatRoomRepository extends ReactiveMongoRepository<ChatRoom, UUID> {
    @Query("{'participants': { $all: ?0 }}")
    Mono<ChatRoom> findByParticipants(Iterable<String> elements);
}
