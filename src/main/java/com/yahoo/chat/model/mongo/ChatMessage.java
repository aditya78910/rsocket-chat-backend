package com.yahoo.chat.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    private UUID id;
    private UUID chatRoomId;
    private String content;
    private String sender;
    private Instant insertTs;
    private Instant updateTs;
}
