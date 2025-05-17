package com.team.webkit.backend.api.chat.controller;

import com.team.webkit.backend.api.chat.dto.ChatMessageDto;
import com.team.webkit.backend.api.chat.dto.ChatMessageRequestDto;
import com.team.webkit.backend.api.chat.dto.NotificationRequestDto;
import com.team.webkit.backend.api.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final ChatService chatService;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessageDto sendMessage(
        @DestinationVariable Long roomId,
        ChatMessageRequestDto req   // ← now this type exists
    ) {
        return chatService.postMessage(roomId, req.getSenderId(), req.getMessage());
    }

    // 알림용
    @MessageMapping("/notify")
    public void sendNotification(NotificationRequestDto req) {
        log.info("📥 알림 요청 도착: receiverId={}, message={}", req.getReceiverId(), req.getMessage());
        chatService.sendNotification(req);
    }


}
