package com.yahoo.chat.mapper;

import com.yahoo.chat.dto.response.ChatMessageDTO;
import com.yahoo.chat.model.mongo.ChatMessage;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ChatMessage_ChatMessageDTO_Mapper {
    public ChatMessageDTO toDTO(ChatMessage chatMessage){

        return  ChatMessageDTO.builder()
                        .senderId(chatMessage.getSender())
                        .content(chatMessage.getContent())
                        .id(chatMessage.getId())
                        .chatRoomId(chatMessage.getChatRoomId())
                        .insertTs(chatMessage.getInsertTs())
                        .updateTs(chatMessage.getUpdateTs())
                        .build();
    }
}
