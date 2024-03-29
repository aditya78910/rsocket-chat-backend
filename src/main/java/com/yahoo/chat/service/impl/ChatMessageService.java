package com.yahoo.chat.service.impl;

import com.yahoo.chat.dto.request.ChatMessageGetRequestDTO;
import com.yahoo.chat.dto.request.RecentChatBuddiesRequest;
import com.yahoo.chat.dto.response.ChatMessageGetResponseDTO;
import com.yahoo.chat.dto.response.RecentChatBuddiesResponse;
import com.yahoo.chat.mapper.ChatMessage_ChatMessageDTO_Mapper;
import com.yahoo.chat.mapper.ChatRoomMapper;
import com.yahoo.chat.model.mongo.ChatRoom;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ChatMessageService {

    ChatMessage_ChatMessageDTO_Mapper chatMessageChatMessageDTOMapper;

    MongoService mongoService;

    ChatRoomMapper chatRoomMapper;


    public ChatMessageService(ChatMessage_ChatMessageDTO_Mapper chatMessageChatMessageDTOMapper, MongoService mongoService, ChatRoomMapper chatRoomMapper) {
        this.chatMessageChatMessageDTOMapper = chatMessageChatMessageDTOMapper;
        this.mongoService = mongoService;
        this.chatRoomMapper = chatRoomMapper;
    }

    public Mono<ChatMessageGetResponseDTO> getPreviousChatMessages(Mono<ChatMessageGetRequestDTO> chatMessageGetRequestDTOMono){
        return chatMessageGetRequestDTOMono
                .flatMapMany(chatMessageGetRequestDTO -> mongoService.getPreviousMessagesPaginated(chatMessageGetRequestDTO))
                .map(chatMessageChatMessageDTOMapper::toDTO)
                .collectList()
                .map(ChatMessageGetResponseDTO::new);
    }

    public Mono<RecentChatBuddiesResponse> getRecentChatBuddies(Mono<RecentChatBuddiesRequest> recentChatBuddiesRequest) {

        return mongoService.findRecentChatBuddies(recentChatBuddiesRequest)




                .map(chatRoomMapper::toDTO)
                .collectList()
                .map(RecentChatBuddiesResponse::new);
    }
}
