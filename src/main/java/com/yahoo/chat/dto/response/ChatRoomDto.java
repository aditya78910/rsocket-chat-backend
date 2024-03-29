package com.yahoo.chat.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class ChatRoomDto {
    private UUID chatRoomId;
    private boolean isGroupChat; //1-1 or Group chat
    private Set<String> participants;
    private Instant insertTs;
    private Instant updateTs;
}
