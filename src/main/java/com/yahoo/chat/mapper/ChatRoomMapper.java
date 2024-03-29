package com.yahoo.chat.mapper;

import com.yahoo.chat.dto.response.ChatRoomDto;
import com.yahoo.chat.model.mongo.ChatRoom;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Component
public class ChatRoomMapper {
    public ChatRoom toDBModel(Set<String> participants, boolean isGroupChat){
        return ChatRoom.builder()
                .id(UUID.randomUUID())
                .isGroupChat(isGroupChat)
                .participants(participants)
                .insertTs(Instant.now())
                .updateTs(Instant.now())
                .build();
    }

    public ChatRoomDto toDTO(ChatRoom chatRoom){
        return ChatRoomDto.builder()
                .chatRoomId(chatRoom.getId())
                .isGroupChat(chatRoom.isGroupChat())
                .participants(chatRoom.getParticipants())
                .insertTs(chatRoom.getInsertTs())
                .updateTs(chatRoom.getUpdateTs())
                .build();
    }
}
