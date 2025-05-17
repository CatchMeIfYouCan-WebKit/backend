package com.team.webkit.backend.api.chat.repository;

import com.team.webkit.backend.api.chat.entity.ChatRoom;
import com.team.webkit.backend.api.chat.entity.ChatRoom.Type;
import com.team.webkit.backend.api.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUser1AndUser2AndTypeAndRelatedId(
        Member user1, Member user2, Type type, Long relatedId);
    List<ChatRoom> findByUser1OrUser2(Member user1, Member user2);
}
