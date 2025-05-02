package com.team.webkit.backend.api.map.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "login_id", nullable = false, unique = true, length = 255)
    private String loginId;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 필요한 경우, User와 연결된 다른 엔티티 (예: Pet, MissingPost 등) 관계 설정도 여기에 추가 가능
    // 예시:
    // @OneToMany(mappedBy = "user")
    // private List<Pet> pets;
}
