package com.yahoo.chat.dto.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageErrorResponse {
    private String errorMessage;
    private String errorType;
    private int category;
}
