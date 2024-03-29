package com.yahoo.chat.service.impl;

import com.yahoo.chat.dto.request.ChatMessageSendRequestDTO;
import com.yahoo.chat.dto.request.ChatSessionInitiateDTO;
import com.yahoo.chat.dto.response.ChatMessageSendResponseDTO;
import com.yahoo.chat.mapper.ChatMessageSendResponse_Mapper;
import com.yahoo.chat.mapper.ChatMessage_ChatMessageDTO_Mapper;
import com.yahoo.chat.mapper.ChatMessage_ChatMessageSendDTO_Mapper;
import com.yahoo.chat.service.LiveChatService;
import com.yahoo.chat.service.PubSubService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Slf4j
@Service
public class LiveChatServiceImpl implements LiveChatService {
    ChatMessage_ChatMessageDTO_Mapper chatMessage_chatMessageDTO_mapper;

    ChatMessage_ChatMessageSendDTO_Mapper chatMessage_chatMessageSendDTO_mapper;

    ChatMessageSendResponse_Mapper chatMessageSendResponse_mapper;
    MongoService databaseService;


    private PubSubService pubSubService;


    public LiveChatServiceImpl(ChatMessage_ChatMessageDTO_Mapper chatMessage_chatMessageDTO_mapper,
                           ChatMessage_ChatMessageSendDTO_Mapper chatMessage_chatMessageSendDTO_mapper,
                           ChatMessageSendResponse_Mapper chatMessageSendResponse_mapper,
                           PubSubService pubSubService, MongoService databaseService) {
        this.chatMessage_chatMessageDTO_mapper = chatMessage_chatMessageDTO_mapper;
        this.chatMessage_chatMessageSendDTO_mapper = chatMessage_chatMessageSendDTO_mapper;
        this.chatMessageSendResponse_mapper = chatMessageSendResponse_mapper;
        this.pubSubService = pubSubService;
        this.databaseService = databaseService;
    }

    public Mono<Void> initiateChatSession(ChatSessionInitiateDTO chatSessionInitiateDTO,
                                          RSocketRequester rSocketRequester) {
        return rSocketRequester.rsocket()
                .onClose()
                .doFirst(() -> log.info("just connected now"))
                .doOnTerminate(() -> log.info("Disconnecting now"));



    }

    public Flux<Object> receiveMessage(Mono<ChatSessionInitiateDTO> chatSessionInitiateDTOMono,
                                       RSocketRequester rSocketRequester) {

        return chatSessionInitiateDTOMono
                .flatMapMany(chatSessionInitiateDTO ->
                        pubSubService.subscribeToChannel(chatSessionInitiateDTO.getUsername()));

    }

    /***
     * Publish the incomming message from UI to redis and save it in the database
     * @param
     * @param chatMessageDtoMono
     * @return
     */
    public Mono<ChatMessageSendResponseDTO> sendSingleMessage(Mono<ChatMessageSendRequestDTO> chatMessageDtoMono) {

        UUID messageId = UUID.randomUUID();

        return chatMessageDtoMono
                .flatMap(chatMessageSendRequestDto -> {
                            if (ObjectUtils.isEmpty(chatMessageSendRequestDto.getChatRoomId())) {
                                return pubSubService.publishSingleMessage(chatMessageSendRequestDto.getReceiver(), chatMessageSendRequestDto)
                                        .zipWith(databaseService.fetchChatRoomAndSaveMessage(chatMessageSendRequestDto, messageId))
                                        .map(tuple -> tuple.mapT2(chatMessage_chatMessageDTO_mapper::toDTO))
                                        .map(chatMessageSendResponse_mapper::toDTO);
                            }else {
                                databaseService.saveMessage(chatMessageSendRequestDto, messageId)
                                        .subscribeOn(Schedulers.boundedElastic())
                                        .subscribe();

                                return pubSubService.publishSingleMessage(chatMessageSendRequestDto.getReceiver(), chatMessageSendRequestDto)
                                        .map(redisResponse -> chatMessageSendResponse_mapper.fromRedisResponse(redisResponse, chatMessageSendRequestDto, messageId));
                            }
                        }

                );

    }
}
