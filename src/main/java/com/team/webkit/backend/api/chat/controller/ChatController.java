package com.team.webkit.backend.api.chat.controller;

import com.team.webkit.backend.api.chat.dto.ChatMessageDto;
import com.team.webkit.backend.api.chat.entity.ChatMessage;
import com.team.webkit.backend.api.chat.entity.ChatRoom;
import com.team.webkit.backend.api.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void handleMessage(ChatMessageDto dto) {
        try {
            // 채팅방 생성/조회 + 메시지 저장
            ChatRoom room = chatService.findOrCreateRoom(dto);
            ChatMessage saved = chatService.saveMessage(room, dto);
            String senderNickname = chatService.getSenderNickname(Integer.parseInt(dto.getSenderId()));

            // 전송용 DTO 생성
            ChatMessageDto responseDto = ChatMessageDto.builder()
                    .senderId(dto.getSenderId())
                    .senderNickname(senderNickname)
                    .receiverId(dto.getReceiverId())
                    .type(dto.getType())
                    .relatedId(dto.getRelatedId())
                    .content(saved.getMessage())
                    .timestamp(saved.getSentAt().toString())
                    .build();

            // ✅ senderId, receiverId 각각의 채널로 전송
            messagingTemplate.convertAndSend("/user/" + dto.getSenderId() + "/queue/messages", responseDto);
            messagingTemplate.convertAndSend("/user/" + dto.getReceiverId() + "/queue/messages", responseDto);

        } catch (Exception e) {
            log.error("❌ 채팅 메시지 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("채팅 메시지 처리 중 오류 발생", e);
        }
    }
}
