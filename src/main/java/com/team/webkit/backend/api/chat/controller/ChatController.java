package com.team.webkit.backend.api.chat.controller;

import com.team.webkit.backend.api.chat.dto.ChatMessageDto;
import com.team.webkit.backend.api.chat.dto.ChatRoomDto;
import com.team.webkit.backend.api.chat.dto.ChatRoomRequest;
import com.team.webkit.backend.api.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    /** 방 생성 또는 조회 */
    @PostMapping("/room")
    public ResponseEntity<ChatRoomDto> getOrCreateRoom(@RequestBody ChatRoomRequest request) {
        var dto = chatService.getOrCreateRoom(request);
        return ResponseEntity.ok(dto);
    }

    /** 한 방의 메시지 조회 */
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessages(@PathVariable Long roomId) {
        var list = chatService.listMessages(roomId);
        return ResponseEntity.ok(list);
    }

    /** 내 채팅방 목록 조회 (마지막 메시지 포함) */
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDto>> listRooms(@RequestParam Long userId) {
        var rooms = chatService.listRooms(userId);
        return ResponseEntity.ok(rooms);
    }

    /** 채팅방 삭제 */
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        chatService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
}
