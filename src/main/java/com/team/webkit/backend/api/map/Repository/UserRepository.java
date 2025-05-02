package com.team.webkit.backend.api.map.Repository;

import com.team.webkit.backend.api.map.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByLoginId(String loginId);
}
