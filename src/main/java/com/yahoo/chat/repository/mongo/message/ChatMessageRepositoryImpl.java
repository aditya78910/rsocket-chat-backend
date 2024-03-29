package com.yahoo.chat.repository.mongo.message;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom{
    void findRecentChatBuddies(String userId, Set<UUID> chatRoomIds){

    }
}
