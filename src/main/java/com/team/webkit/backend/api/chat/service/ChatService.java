package com.team.webkit.backend.api.chat.service;

import com.team.webkit.backend.api.chat.dto.ChatMessageDto;
import com.team.webkit.backend.api.chat.entity.ChatMessage;
import com.team.webkit.backend.api.chat.entity.ChatRoom;
import com.team.webkit.backend.api.chat.entity.ChatRoom.ChatType;
import com.team.webkit.backend.api.chat.repository.ChatMessageRepository;
import com.team.webkit.backend.api.chat.repository.ChatRoomRepository;
import com.team.webkit.backend.api.member.entity.Member;
import com.team.webkit.backend.api.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ChatRoom findOrCreateRoom(ChatMessageDto dto) {
        Integer rawUser1Id = Integer.valueOf(dto.getSenderId());
        Integer rawUser2Id = Integer.valueOf(dto.getReceiverId());
        Long relatedId = Long.valueOf(dto.getRelatedId());
        ChatType type = ChatType.valueOf(dto.getType().toUpperCase());

        // 항상 낮은 ID가 user1이 되도록 정렬
        Integer user1Id = rawUser1Id;
        Integer user2Id = rawUser2Id;
        if (user1Id > user2Id) {
            Integer temp = user1Id;
            user1Id = user2Id;
            user2Id = temp;
        }

        final Integer finalUser1Id = user1Id;
        final Integer finalUser2Id = user2Id;

        return chatRoomRepository.findByUser1IdAndUser2IdAndTypeAndRelatedId(
                finalUser1Id.longValue(), finalUser2Id.longValue(), type, relatedId
        ).orElseGet(() -> {
            Member user1 = memberRepository.findById(finalUser1Id).orElseThrow(() -> new IllegalArgumentException("user1 없음"));
            Member user2 = memberRepository.findById(finalUser2Id).orElseThrow(() -> new IllegalArgumentException("user2 없음"));

            ChatRoom newRoom = ChatRoom.builder()
                    .user1(user1)
                    .user2(user2)
                    .type(type)
                    .relatedId(relatedId)
                    .build();
            return chatRoomRepository.save(newRoom);
        });
    }

    @Transactional
    public ChatMessage saveMessage(ChatRoom room, ChatMessageDto dto) {
        Integer senderId = Integer.valueOf(dto.getSenderId());
        Member sender = memberRepository.findById(senderId).orElseThrow(() -> new IllegalArgumentException("보낸 유저 없음"));

        return chatMessageRepository.save(ChatMessage.builder()
                .room(room)
                .sender(sender)
                .message(dto.getContent())
                .build());
    }

    public List<ChatMessage> getMessagesByRoom(ChatRoom room) {
        return chatMessageRepository.findByRoomOrderBySentAtAsc(room);
    }
    public String getSenderNickname(Integer senderId) {
        return memberRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"))
                .getNickname(); // Member 엔티티에 nickname 필드가 있다고 가정
    }

}
