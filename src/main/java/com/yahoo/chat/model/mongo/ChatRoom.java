package com.yahoo.chat.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    private UUID id;
    private boolean isGroupChat; //1-1 or Group chat

    @Indexed
    private Set<String> participants;
    private Instant insertTs;
    private Instant updateTs;
}
