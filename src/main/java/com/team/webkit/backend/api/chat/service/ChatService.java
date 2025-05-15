package com.team.webkit.backend.api.chat.service;

import com.team.webkit.backend.api.chat.dto.*;
import com.team.webkit.backend.api.chat.entity.ChatMessage;
import com.team.webkit.backend.api.chat.entity.ChatRoom;
import com.team.webkit.backend.api.chat.repository.ChatMessageRepository;
import com.team.webkit.backend.api.chat.repository.ChatRoomRepository;
import com.team.webkit.backend.api.adopt.entity.AdoptPost;
import com.team.webkit.backend.api.adopt.repository.AdoptPostRepository;
import com.team.webkit.backend.api.member.entity.Member;
import com.team.webkit.backend.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final AdoptPostRepository adoptPostRepository;

    // ✅ 1. 채팅방 조회 or 생성
    @Transactional
    public ChatRoomResponse getOrCreateRoom(ChatRoomRequest request) {
        Member sender = memberRepository.findById(request.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("보낸 회원 없음"));
        Member receiver = memberRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("받는 회원 없음"));
        AdoptPost adoptPost = adoptPostRepository.findById(request.getAdoptPostId())
                .orElseThrow(() -> new IllegalArgumentException("입양 게시글 없음"));

        // ✅ 여기에서 두 방향 모두 확인 (역방향 메서드 사용되는 부분!)
        ChatRoom room = chatRoomRepository
                .findBySenderAndReceiverAndAdoptPost(sender, receiver, adoptPost)
                .or(() -> chatRoomRepository.findBySenderAndReceiverAndAdoptPost(receiver, sender, adoptPost))
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.builder()
                        .sender(sender)
                        .receiver(receiver)
                        .adoptPost(adoptPost)
                        .build()));

        return ChatRoomResponse.builder()
                .roomId(room.getId())
                .senderId(sender.getId())
                .receiverId(receiver.getId())
                .adoptPostId(adoptPost.getId())
                .build();
    }



    // ✅ 2. 메시지 저장
    @Transactional
    public ChatMessageResponse saveMessage(ChatMessageRequest request) {
        ChatRoom room = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));

        Member sender = memberRepository.findById(request.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("보낸 회원 없음"));

        ChatMessage message = chatMessageRepository.save(ChatMessage.builder()
                .room(room)
                .sender(sender)
                .message(request.getMessage())
                .build());

        return ChatMessageResponse.builder()
                .roomId(room.getId())
                .senderId(sender.getId())
                .senderNickname(sender.getNickname())
                .message(message.getMessage())
                .sentAt(message.getSentAt())
                .build();
    }

    // ✅ 3. 채팅방 내 메시지 목록 조회
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessagesByRoom(Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방 없음"));

        List<ChatMessage> messages = chatMessageRepository.findByRoomOrderBySentAtAsc(room);

        return messages.stream().map(msg ->
                ChatMessageResponse.builder()
                        .roomId(room.getId())
                        .senderId(msg.getSender().getId())
                        .senderNickname(msg.getSender().getNickname())
                        .message(msg.getMessage())
                        .sentAt(msg.getSentAt())
                        .build()
        ).collect(Collectors.toList());
    }
}

