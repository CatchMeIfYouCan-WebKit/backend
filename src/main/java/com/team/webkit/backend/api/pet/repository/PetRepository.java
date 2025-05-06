package com.team.webkit.backend.api.pet.repository;

import com.team.webkit.backend.api.member.entity.Member;
import com.team.webkit.backend.api.pet.entity.Pet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Integer> {

    List<Pet> findByMember(Member member);

    List<Pet> findByMemberAndNameContaining(Member member, String name);

    List<Pet> findByMemberAndBreedContaining(Member member, String breed);

    List<Pet> findByMemberAndNameContainingAndBreedContaining(Member member, String name,
        String breed);

    boolean existsByRegistrationNumber(String regNum);
}
