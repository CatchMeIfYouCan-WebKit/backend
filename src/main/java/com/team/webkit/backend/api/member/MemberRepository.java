package com.team.webkit.backend.api.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    boolean existsByLoginId(String loginId);

    boolean existsByPhone(String phone);

    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findByPhone(String phone);

    Member findByLoginIdAndNicknameAndPhone(String loginId, String nickname, String phone);
}
