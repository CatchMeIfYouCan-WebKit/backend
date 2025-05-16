package com.team.webkit.backend.api.chat.controller;

import com.team.webkit.backend.api.chat.dto.ChatMessageDto;
import com.team.webkit.backend.api.chat.dto.ChatRoomDto;
import com.team.webkit.backend.api.chat.dto.ChatRoomRequest;
import com.team.webkit.backend.api.chat.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    // 채팅방 조회 or 생성
    @PostMapping("/room")
    public ResponseEntity<ChatRoomDto> getOrCreateRoom(
        @RequestBody ChatRoomRequest request
    ) {
        ChatRoomDto response = chatService.getOrCreateRoom(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessages(
        @PathVariable Long roomId
    ) {
        List<ChatMessageDto> list = chatService.listMessages(roomId);
        return ResponseEntity.ok(list);
    }

    // (신규) 내 채팅방 목록 조회
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDto>> listRooms(@RequestParam Long userId) {
        return ResponseEntity.ok(chatService.listRooms(userId));
    }

    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        chatService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}
