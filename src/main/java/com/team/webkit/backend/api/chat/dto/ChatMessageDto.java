// ChatMessageDto.java
package com.team.webkit.backend.api.chat.dto;

import com.team.webkit.backend.api.chat.entity.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatMessageDto {
    private Long id;
    private Long roomId;
    private Long senderId;
    private String message;
    private LocalDateTime sentAt;

    // ChatMessageDto.java
    public static ChatMessageDto from(ChatMessage e) {
        return ChatMessageDto.builder()
            .id(e.getId())                           // Long
            .roomId(e.getRoom().getId())             // Long
            // Integer → Long 변환
            .senderId(e.getSender().getId().longValue())
            .message(e.getMessage())
            .sentAt(e.getSentAt())
            .build();
    }

}
