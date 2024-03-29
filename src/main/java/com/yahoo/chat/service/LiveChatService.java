package com.yahoo.chat.service;

import com.yahoo.chat.dto.request.ChatMessageSendRequestDTO;
import com.yahoo.chat.dto.request.ChatSessionInitiateDTO;
import com.yahoo.chat.dto.response.ChatMessageSendResponseDTO;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public interface LiveChatService {

    Mono<Void> initiateChatSession(ChatSessionInitiateDTO chatSessionInitiateDTO,
                                   RSocketRequester rSocketRequester);

    Flux<Object> receiveMessage(Mono<ChatSessionInitiateDTO> chatSessionInitiateDTOMono,
                                RSocketRequester rSocketRequester);

    Mono<ChatMessageSendResponseDTO> sendSingleMessage(Mono<ChatMessageSendRequestDTO> chatMessageDtoMono);

    }
