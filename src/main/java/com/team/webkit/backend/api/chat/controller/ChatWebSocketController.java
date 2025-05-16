package com.team.webkit.backend.api.chat.controller;

import com.team.webkit.backend.api.chat.dto.ChatMessageDto;
import com.team.webkit.backend.api.chat.dto.ChatMessageRequestDto;  // ← import here
import com.team.webkit.backend.api.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
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
}
