package com.yahoo.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecentChatBuddiesResponse {
    private List<ChatRoomDto> chatRooms;
}
