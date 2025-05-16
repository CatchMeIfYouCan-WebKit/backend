package com.team.webkit.backend.api.chat.service;

import com.team.webkit.backend.api.chat.dto.ChatMessageDto;
import com.team.webkit.backend.api.chat.dto.ChatRoomDto;
import com.team.webkit.backend.api.chat.dto.ChatRoomRequest;
import com.team.webkit.backend.api.chat.entity.ChatMessage;
import com.team.webkit.backend.api.chat.entity.ChatRoom;
import com.team.webkit.backend.api.chat.repository.ChatMessageRepository;
import com.team.webkit.backend.api.chat.repository.ChatRoomRepository;
import com.team.webkit.backend.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository roomRepo;
    private final ChatMessageRepository msgRepo;
    private final MemberRepository memberRepo;

    @Transactional(readOnly = true)
    public List<ChatRoomDto> listRooms(Long userId) {
        var m = memberRepo.findById(userId.intValue())
            .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + userId));
        return roomRepo.findByUser1OrUser2(m, m).stream()
            .map(ChatRoomDto::from)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDto> listMessages(Long roomId) {
        var room = roomRepo.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("채팅방 없음: " + roomId));
        return msgRepo.findByRoomOrderBySentAtAsc(room).stream()
            .map(ChatMessageDto::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public ChatMessageDto postMessage(Long roomId, Long senderId, String message) {
        var room = roomRepo.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("채팅방 없음: " + roomId));
        var sender = memberRepo.findById(senderId.intValue())
            .orElseThrow(() -> new IllegalArgumentException("회원 없음: " + senderId));
        var msg = ChatMessage.builder()
            .room(room)
            .sender(sender)
            .message(message)
            .build();
        return ChatMessageDto.from(msgRepo.save(msg));
    }

    // ───────────────────────────────────────────────────────────
    // ★ 여기부터 추가된 오버로드 메서드
    @Transactional
    public ChatRoomDto getOrCreateRoom(ChatRoomRequest req) {
        return getOrCreateRoom(
            req.getUser1Id(),
            req.getUser2Id(),
            req.getType(),
            req.getRelatedId()
        );
    }
    // ───────────────────────────────────────────────────────────

    @Transactional
    public ChatRoomDto getOrCreateRoom(Long user1Id, Long user2Id, String type, Long relatedId) {
        var m1 = memberRepo.findById(user1Id.intValue())
            .orElseThrow(() -> new IllegalArgumentException("회원1 없음: " + user1Id));
        var m2 = memberRepo.findById(user2Id.intValue())
            .orElseThrow(() -> new IllegalArgumentException("회원2 없음: " + user2Id));
        var tp = ChatRoom.Type.valueOf(type);

        return roomRepo.findByUser1AndUser2AndTypeAndRelatedId(m1, m2, tp, relatedId)
            .or(() -> roomRepo.findByUser1AndUser2AndTypeAndRelatedId(m2, m1, tp, relatedId))
            .map(ChatRoomDto::from)
            .orElseGet(() -> {
                var room = ChatRoom.builder()
                    .user1(m1).user2(m2)
                    .type(tp).relatedId(relatedId)
                    .build();
                return ChatRoomDto.from(roomRepo.save(room));
            });
    }

    /** 채팅방 삭제 */
    @Transactional
    public void deleteRoom(Long roomId) {
        // 1) 방을 조회해서
        ChatRoom room = roomRepo.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("채팅방 없음: " + roomId));
        // 2) 해당 방의 모든 메시지를 먼저 삭제
        msgRepo.deleteByRoom(room);
        // 3) 그 다음 방을 삭제
        roomRepo.delete(room);
    }
}
