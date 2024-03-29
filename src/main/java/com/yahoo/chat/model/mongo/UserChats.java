package com.yahoo.chat.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

/**
 * This model will contain the ids of all the chatrooms a user been part of till now.
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "userChats")
public class UserChats {
    @Id
    private String userId;

    private Set<UUID> chatRooms;


}
