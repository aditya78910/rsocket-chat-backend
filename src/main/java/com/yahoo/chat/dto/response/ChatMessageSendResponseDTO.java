package com.yahoo.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessageSendResponseDTO {
    Long sendResp;
    UUID messageId;
    UUID chatRoomId;
}
