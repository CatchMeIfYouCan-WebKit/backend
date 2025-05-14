package com.team.webkit.backend.api.chat.repository;

import com.team.webkit.backend.api.chat.entity.ChatMessage;
import com.team.webkit.backend.api.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 채팅방 내 메시지 전체 조회 (시간순)
    List<ChatMessage> findByRoomOrderBySentAtAsc(ChatRoom room);
}
