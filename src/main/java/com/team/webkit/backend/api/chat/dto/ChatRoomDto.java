package com.team.webkit.backend.api.chat.dto;

import com.team.webkit.backend.api.chat.entity.ChatMessage;
import com.team.webkit.backend.api.chat.entity.ChatRoom;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDto {
    private Long id;
    private Integer user1Id;
    private Integer user2Id;
    private String type;
    private Long relatedId;
    private LocalDateTime createdAt;

    // 마지막 메시지 내용 + 시간
    private String lastMessage;
    private LocalDateTime lastMessageAt;

    public static ChatRoomDto from(ChatRoom e) {
        ChatRoomDto dto = ChatRoomDto.builder()
            .id(e.getId())
            .user1Id(e.getUser1().getId())
            .user2Id(e.getUser2().getId())
            .type(e.getType().name())
            .relatedId(e.getRelatedId())
            .createdAt(e.getCreatedAt())
            .build();

        // 메시지 리스트에서 마지막 요소 꺼내서 세팅
        if (e.getMessages() != null && !e.getMessages().isEmpty()) {
            ChatMessage last = e.getMessages().get(e.getMessages().size() - 1);
            dto.setLastMessage(last.getMessage());
            dto.setLastMessageAt(last.getSentAt());
        }
        return dto;
    }
}
