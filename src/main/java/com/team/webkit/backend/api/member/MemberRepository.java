package com.team.webkit.backend.api.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    boolean existsByLoginId(String loginId);

    boolean existsByNickname(String nickname);

    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findByPhone(String phone);

    Optional<Member> findById(Integer id);

    Member findByLoginIdAndNicknameAndPhone(String loginId, String nickname, String phone);
}
