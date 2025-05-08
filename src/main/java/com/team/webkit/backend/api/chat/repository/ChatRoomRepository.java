package com.team.webkit.backend.api.chat.repository;

import com.team.webkit.backend.api.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUser1IdAndUser2IdAndTypeAndRelatedId(Long user1Id, Long user2Id, ChatRoom.ChatType type, Long relatedId);
}
