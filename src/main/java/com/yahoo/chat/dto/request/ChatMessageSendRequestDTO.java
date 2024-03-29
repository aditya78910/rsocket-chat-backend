package com.yahoo.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessageSendRequestDTO {
    private String senderId;
    private String receiver;
    private String content;
    private UUID chatRoomId;

}

