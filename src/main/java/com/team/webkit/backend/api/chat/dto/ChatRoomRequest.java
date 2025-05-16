package com.team.webkit.backend.api.chat.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChatRoomRequest {
    private Long user1Id;
    private Long user2Id;
    private String type;    // "ADOPTION" or "VET"
    private Long relatedId; // 게시글 ID 등
}
