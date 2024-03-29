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
public class ChatMessageGetRequestDTO {
    private String username1;
    private String username2;
    private UUID chatRoomId;
    private Integer pageNumber;
}
