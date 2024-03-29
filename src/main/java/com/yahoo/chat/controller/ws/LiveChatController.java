package com.yahoo.chat.controller.ws;


import com.yahoo.chat.dto.error.MessageErrorResponse;
import com.yahoo.chat.dto.request.ChatMessageSendRequestDTO;
import com.yahoo.chat.dto.request.ChatSessionInitiateDTO;
import com.yahoo.chat.dto.response.ChatMessageSendResponseDTO;
import com.yahoo.chat.service.LiveChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
public class LiveChatController {
    @Autowired
    LiveChatService liveChatService;

    RSocketMessageHandler rSocketMessageHandler;

    /*@ConnectMapping({"/initiate"})
    public Mono<Void> initiateChatSession(@Payload ChatSessionInitiateDTO chatSessionInitiateDTO,
                                          RSocketRequester rSocketRequester)  {
        log.info("invoked /initiate with request body {}", chatSessionInitiateDTO);
        return liveChatService.initiateChatSession(chatSessionInitiateDTO, rSocketRequester);
    }*/

    //TODO - authentication and authorization
    /**
     * The logged-in user can use this endpoint to subscribe to the continuous stream of messages sent by other users to him.
     * The subscription would be to a channel (exclusively for this logged-in user) where the messages
     * would be published by other users.
     * @param chatMessageDto - the username of the user
     * @return
     */
    @MessageMapping({"/chat.receiveMessage"})
    public Flux<Object> receiveMessage(Mono<ChatSessionInitiateDTO> chatMessageDto)  {

        return liveChatService.receiveMessage(chatMessageDto, null);
    }

    /**
     * Send a message to another user. This will publish the message on the user's channel as well
     * as persist the message asynchronously.
     *
     * @param chatMessageDtoMono
     * @return
     */
    @MessageMapping("/chat.sendSingleMessage")
    public Mono<ChatMessageSendResponseDTO> sendSingleMessage(Mono<ChatMessageSendRequestDTO> chatMessageDtoMono)  {

        return liveChatService.sendSingleMessage(chatMessageDtoMono);
    }


    // TODO : to decide if this way of sending messages is needed
    /*
    @MessageMapping("/chat.sendSingleMessageWithNonBlockingFluxResponse/{chatSessionId}")
    public Flux<Object> sendSingleMessageWithNonBlockingFluxResponse(@DestinationVariable String chatSessionId,
                                                              Mono<ChatMessageSendRequestDTO> chatMessageDtoMono,
                                                              RSocketRequester rSocketRequester) throws Exception {

        return liveChatService.sendSingleMessageWithNonBlockingFluxResponse(chatSessionId, chatMessageDtoMono);

    }*/

    @MessageExceptionHandler(value={RuntimeException.class})
    public MessageErrorResponse serverExceptionHandler(RuntimeException ex) {
        log.error("this time inside the controller advice", ex);
        return MessageErrorResponse.builder()
                .errorMessage(ex.getMessage())
                .build();
    }
}
