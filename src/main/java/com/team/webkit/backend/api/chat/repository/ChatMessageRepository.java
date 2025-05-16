// ChatMessageRepository.java
package com.team.webkit.backend.api.chat.repository;

import com.team.webkit.backend.api.chat.entity.ChatMessage;
import com.team.webkit.backend.api.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomOrderBySentAtAsc(ChatRoom room);

    // 방 단위로 메시지 모두 삭제
    void deleteByRoom(ChatRoom room);
}
