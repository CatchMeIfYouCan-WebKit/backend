package com.team.webkit.backend.api.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {

    private Long roomId;           // 채팅방 ID
    private Integer senderId;      // 메시지 보낸 사람 ID
    private String message;        // 메시지 내용
}
