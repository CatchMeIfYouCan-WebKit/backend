package com.team.webkit.backend.api.member.service;

import com.team.webkit.backend.api.member.entity.Member;
import com.team.webkit.backend.api.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public String join(Member member) {
        log.info("MemberService join : 회원가입");

        if (memberRepository.existsByLoginId(member.getLoginId())) {
            return "2002";
        }

        memberRepository.save(member);

        return "0000";
    }

    public boolean out(Integer id) {
        Optional<Member> optionalMember = memberRepository.findById(id);

        if (optionalMember.isPresent()) {
            memberRepository.delete(optionalMember.get());
            return true;
        }

        return false;
    }

    public Member login(String loginId, String password) {
        log.info("MemberService login : 로그인");

        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);

        if (optionalMember.isEmpty()) {
            return null;
        }

        Member member = optionalMember.get();

        if (!member.getPassword().equals(password)) {
            return null;
        }

        return member;
    }

    public String duplicate(String loginId) {
        log.info("MemberService duplicate : 아이디 중복 검사");

        if (memberRepository.existsByLoginId(loginId)) {
            return "2002";
        }

        return "0000";
    }


    public String findId(String phone) {
        log.info("MemberService findID : 아이디 찾기");

        Optional<Member> optionalMember = memberRepository.findByPhone(phone);

        return optionalMember.map(Member::getLoginId).orElse(null);

    }

    public String chkPwd(String loginId, String password) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            if (member.getPassword().equals(password)) {
                return "0000";
            } else {
                return "2001";
            }
        } else {
            return "2003";
        }
    }

    public boolean password(Integer id, String password) {
        Optional<Member> optionalMember = memberRepository.findById(id);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            member.setPassword(password);
            memberRepository.save(member);

            return true;
        } else {
            return false;
        }
    }

    public boolean passwordByLoginId(String loginId, String password) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);

        if (optionalMember.isEmpty()) {
            return false;
        }

        Member member = optionalMember.get();
        member.setPassword(password); // 필요 시 비밀번호 암호화 처리
        memberRepository.save(member);

        return true;
    }


    public String duplicateNickname(String nickname) {
        log.info("duplicateNickname : 닉네임 중복 검사");

        if (memberRepository.existsByNickname(nickname)) {
            return "2002";
        }

        return "0000";
    }

    public Member info(String loginId) {
        return memberRepository.findByLoginId(loginId).orElse(null);
    }

    public boolean update(Integer id, String nickname,
        String phone) {
        Optional<Member> optionalMember = memberRepository.findById(id);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            member.setNickname(nickname);
            member.setPhone(phone);
            memberRepository.save(member);

            return true;
        } else {
            return false;
        }
    }


    public boolean find(String loginId, String nickname, String phone) {
        Member member = memberRepository.findByLoginIdAndNicknameAndPhone(loginId, nickname, phone);

        return member != null;
    }


    // id로 회원찾기
    public Member findById(Integer id) {
        return memberRepository.findById(id).orElse(null);
    }


}
