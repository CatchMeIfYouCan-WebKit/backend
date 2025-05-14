package com.team.webkit.backend.api.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponse {

    private Long roomId;           // 채팅방 ID
    private Integer senderId;      // 채팅 시작자 ID
    private Integer receiverId;    // 대상자 ID
    private Long adoptPostId;      // 입양 게시글 ID
}
