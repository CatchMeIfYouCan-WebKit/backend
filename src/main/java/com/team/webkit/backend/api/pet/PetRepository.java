package com.team.webkit.backend.api.pet;

import com.team.webkit.backend.api.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Integer> {

    List<Pet> findByMember(Member member);

}
