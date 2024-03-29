package com.yahoo.chat.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ChatMessageDTO {
    private UUID id;
    private UUID chatRoomId;
    private String content;
    private String senderId;
    private Instant insertTs;
    private Instant updateTs;
}
