package com.yahoo.chat.controller.ws;

import com.yahoo.chat.dto.request.ChatMessageGetRequestDTO;
import com.yahoo.chat.dto.request.RecentChatBuddiesRequest;
import com.yahoo.chat.dto.response.ChatMessageGetResponseDTO;
import com.yahoo.chat.dto.response.RecentChatBuddiesResponse;
import com.yahoo.chat.model.mongo.ChatRoom;
import com.yahoo.chat.service.impl.ChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Controller
public class ChatMessageController {

    @Autowired
    ChatMessageService chatMessageService;

    /**
     * Get the previous chat messages between 2 users. This is a paginated API which
     * would return 10 messages at a time.
     * @param chatMessageGetRequestDTOMono
     * @return
     */
    @MessageMapping({"/chatroom.previousMessages"})
    public Mono<ChatMessageGetResponseDTO> getPreviousMessagesOfChatRoomPaginated(Mono<ChatMessageGetRequestDTO> chatMessageGetRequestDTOMono)  {

        return chatMessageService.getPreviousChatMessages(chatMessageGetRequestDTOMono);
    }

    @MessageMapping({"/users.recentchatbuddies"})
    public Mono<RecentChatBuddiesResponse> getRecentChatBuddies(Mono<RecentChatBuddiesRequest> recentChatBuddiesRequest){
        return chatMessageService.getRecentChatBuddies(recentChatBuddiesRequest);
    }
}
