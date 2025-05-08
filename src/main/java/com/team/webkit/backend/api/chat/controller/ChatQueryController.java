package com.team.webkit.backend.api.chat.controller;

import com.team.webkit.backend.api.chat.dto.ChatMessageDto;
import com.team.webkit.backend.api.chat.entity.ChatRoom;
import com.team.webkit.backend.api.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatQueryController {

    private final ChatService chatService;

    @GetMapping("/messages")
    public List<ChatMessageDto> getMessages(
            @RequestParam String roomType,
            @RequestParam String relatedId,
            @RequestParam String senderId,
            @RequestParam String receiverId
    ) {
        // 채팅방 조회 (있으면 가져오고, 없으면 생성)
        ChatRoom room = chatService.findOrCreateRoom(
                ChatMessageDto.builder()
                        .type(roomType)
                        .relatedId(relatedId)
                        .senderId(senderId)
                        .receiverId(receiverId)
                        .build()
        );

        // 메시지 가져오기
        return chatService.getMessagesByRoom(room).stream()
                .map(msg -> ChatMessageDto.builder()
                        .senderId(msg.getSender().getId().toString())
                        .senderNickname(msg.getSender().getNickname())
                        .receiverId(receiverId)
                        .type(roomType)
                        .relatedId(relatedId)
                        .content(msg.getMessage())
                        .timestamp(msg.getSentAt().toString())
                        .build()
                ).collect(Collectors.toList());
    }
}
