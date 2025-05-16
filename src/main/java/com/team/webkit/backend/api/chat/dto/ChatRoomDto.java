package com.team.webkit.backend.api.chat.dto;

import com.team.webkit.backend.api.chat.entity.ChatRoom;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDto {
    private Long   id;        // ChatRoom.id 는 Long 그대로
    private Integer user1Id;  // ← Integer 로 변경
    private Integer user2Id;
    private String  type;
    private Long    relatedId;
    private LocalDateTime createdAt;

    public static ChatRoomDto from(ChatRoom e) {
        return ChatRoomDto.builder()
            .id(e.getId())
            .user1Id(e.getUser1().getId())   // 이제 타입 불일치 없음
            .user2Id(e.getUser2().getId())
            .type(e.getType().name())
            .relatedId(e.getRelatedId())
            .createdAt(e.getCreatedAt())
            .build();
    }
}
