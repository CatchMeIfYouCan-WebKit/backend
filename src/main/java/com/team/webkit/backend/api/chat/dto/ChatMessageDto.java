package com.team.webkit.backend.api.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {

    private String senderId;     // 보내는 사람 ID
    private String receiverId;   // 받는 사람 ID
    private String type;         // 채팅 목적: ADOPTION, VET
    private String relatedId;    // 게시글 ID 또는 진료 예약 ID
    private String content;      // 메시지 내용
    private String timestamp;    // 보낸 시간
    private String senderNickname; // users.nickname
}
