package com.yahoo.chat.dto.request;

import lombok.Data;

@Data
public class RecentChatBuddiesRequest {
    private String username;
    private Integer pageNumber;
}
