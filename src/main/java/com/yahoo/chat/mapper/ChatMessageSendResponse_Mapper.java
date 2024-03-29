package com.yahoo.chat.mapper;

import com.yahoo.chat.dto.request.ChatMessageSendRequestDTO;
import com.yahoo.chat.dto.response.ChatMessageDTO;
import com.yahoo.chat.dto.response.ChatMessageSendResponseDTO;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;

import java.util.UUID;

@Component
public class ChatMessageSendResponse_Mapper {
    public ChatMessageSendResponseDTO toDTO(Tuple2<Long, ChatMessageDTO> chatMessage){

        return ChatMessageSendResponseDTO.builder()
                .sendResp(chatMessage.getT1())
                .chatRoomId(chatMessage.getT2().getChatRoomId())
                .messageId(chatMessage.getT2().getId())
                .build();
    }

    public ChatMessageSendResponseDTO fromRedisResponse(Long publishedResponse,
                                                        ChatMessageSendRequestDTO chatMessageSendRequestDTO,
                                                        UUID messageId){
        return ChatMessageSendResponseDTO.builder()
                .sendResp(publishedResponse)
                .chatRoomId(chatMessageSendRequestDTO.getChatRoomId())
                .messageId(messageId)
                .build();
    }
}
