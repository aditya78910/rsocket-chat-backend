package com.yahoo.chat.repository.mongo.userchats;

import com.mongodb.client.result.UpdateResult;
import reactor.core.publisher.Flux;

import java.util.Set;
import java.util.UUID;

public interface UserChatsRepositoryCustom {
    Flux<UpdateResult> updateUserChat(Set<String> userId, UUID chatRoomId);
}
