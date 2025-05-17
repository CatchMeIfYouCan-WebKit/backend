package com.team.webkit.backend.api.chat.dto;

import lombok.Data;

@Data
public class NotificationRequestDto {

    // 알림용 Dto
    private Long receiverId;
    private String message;
}
