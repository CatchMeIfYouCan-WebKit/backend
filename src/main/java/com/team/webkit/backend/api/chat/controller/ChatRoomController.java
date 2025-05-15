package com.team.webkit.backend.api.chat.controller;

import com.team.webkit.backend.api.chat.dto.ChatRoomRequest;
import com.team.webkit.backend.api.chat.dto.ChatRoomResponse;
import com.team.webkit.backend.api.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;

    // ✅ 채팅방 조회 or 생성
    @PostMapping("/room")
    public ResponseEntity<ChatRoomResponse> getOrCreateRoom(@RequestBody ChatRoomRequest request) {
        ChatRoomResponse response = chatService.getOrCreateRoom(request);
        return ResponseEntity.ok(response);
    }
}
