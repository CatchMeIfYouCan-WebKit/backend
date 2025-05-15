package com.team.webkit.backend.api.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageResponse {

    private Long roomId;           // 채팅방 ID
    private Integer senderId;      // 보낸 사람 ID
    private String senderNickname; // 보낸 사람 닉네임 (선택 사항)
    private String message;        // 메시지 내용
    private LocalDateTime sentAt;  // 보낸 시간
}
