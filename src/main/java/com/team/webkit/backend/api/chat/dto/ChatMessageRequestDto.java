// ChatMessageDto.java
package com.team.webkit.backend.api.chat.dto;

import com.team.webkit.backend.api.chat.entity.ChatMessage;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatMessageRequestDto {
    private Long id;
    private Long roomId;
    private Long senderId;
    private String message;
    private LocalDateTime sentAt;

    // ChatMessageDto.java
    public static ChatMessageRequestDto from(ChatMessage e) {
        return ChatMessageRequestDto.builder()
            .id(e.getId())                           // Long
            .roomId(e.getRoom().getId())             // Long
            // Integer → Long 변환
            .senderId(e.getSender().getId().longValue())
            .message(e.getMessage())
            .sentAt(e.getSentAt())
            .build();
    }

}
