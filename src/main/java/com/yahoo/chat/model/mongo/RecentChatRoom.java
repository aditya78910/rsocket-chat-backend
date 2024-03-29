package com.yahoo.chat.model.mongo;


import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
public class RecentChatRoom {
    @Id
    private UUID chatRoomId;

    private Instant mostRecent;

}
