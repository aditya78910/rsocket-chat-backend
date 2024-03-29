package com.yahoo.chat.mapper;

import com.yahoo.chat.dto.request.ChatMessageSendRequestDTO;
import com.yahoo.chat.model.mongo.ChatMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.util.UUID;

@Component
public class ChatMessage_ChatMessageSendDTO_Mapper {

    public ChatMessage toDBModel(ChatMessageSendRequestDTO chatMessageDTO,
                                 UUID messageId){

        return  ChatMessage.builder()
                .id(ObjectUtils.isEmpty(messageId)? UUID.randomUUID() :messageId)
                .chatRoomId(chatMessageDTO.getChatRoomId())
                .content(chatMessageDTO.getContent())
                .sender(chatMessageDTO.getSenderId())
                .insertTs(Instant.now())
                .updateTs(Instant.now())
                .build();
    }
}
