package com.team.webkit.backend.api.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequest {

    private Integer senderId;      // 채팅 요청자 (로그인한 사용자)
    private Integer receiverId;    // 채팅 대상자 (입양 글 작성자)
    private Long adoptPostId;      // 어떤 입양 게시글에서의 채팅인지
}
