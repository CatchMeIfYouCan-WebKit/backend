package com.team.webkit.backend.api.chat.repository;

import com.team.webkit.backend.api.chat.entity.ChatRoom;
import com.team.webkit.backend.api.adopt.entity.AdoptPost;
import com.team.webkit.backend.api.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    // 채팅방 중복 방지용 조회 메서드
    Optional<ChatRoom> findBySenderAndReceiverAndAdoptPost(Member sender, Member receiver, AdoptPost adoptPost);
}
