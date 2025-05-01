package com.team.webkit.backend.api.pet;

import com.team.webkit.backend.api.member.Member;
import com.team.webkit.backend.api.member.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PetService {

    private final PetRepository petRepository;
    private final MemberRepository memberRepository;

    public Pet add(Pet pet, Integer userId) {
        log.info("PetService add : 반려동물 등록");

        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        pet.setMember(member);

        return petRepository.save(pet);
    }

    public List<Pet> findAll(Integer userId) {
        log.info("PetService findAll : 내 반려동물 전체 조회");

        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        return petRepository.findByMember(member);
    }

    public Pet findById(Integer id, Integer userId) {
        log.info("PetService findById : 내 반려동물 상세 조회");

        Pet pet = petRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("해당 반려동물을 찾을 수 없습니다"));

        if (!pet.getMember().getId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        return pet;
    }

    public Pet update(Integer id, Pet updated, Integer userId) {
        log.info("PetService update : 반려동물 수정");

        Pet pet = findById(id, userId); // 권한 체크 포함

        pet.setPhotoPath(updated.getPhotoPath());
        pet.setName(updated.getName());
        pet.setSpecies(updated.getSpecies());
        pet.setCoatColor(updated.getCoatColor());
        pet.setIsNeutered(updated.getIsNeutered());
        pet.setDateOfBirth(updated.getDateOfBirth());
        pet.setAge(updated.getAge());
        pet.setWeight(updated.getWeight());
        pet.setRegistrationNumber(updated.getRegistrationNumber());

        return petRepository.save(pet);
    }

    public void delete(Integer id, Integer memberId) {
        log.info("PetService delete : 반려동물 삭제");

        Pet pet = findById(id, memberId); // 권한 체크 포함
        petRepository.delete(pet);
    }
}
