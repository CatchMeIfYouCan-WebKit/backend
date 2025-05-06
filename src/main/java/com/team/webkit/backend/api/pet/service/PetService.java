package com.team.webkit.backend.api.pet.service;

import com.team.webkit.backend.api.member.entity.Member;
import com.team.webkit.backend.api.member.repository.MemberRepository;
import com.team.webkit.backend.api.pet.dto.PetResponseDto;
import com.team.webkit.backend.api.pet.entity.Pet;
import com.team.webkit.backend.api.pet.repository.PetRepository;
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

    // 반려동물 등록
    public Pet add(Pet pet, Integer userId) {
        log.info("PetService add : 반려동물 등록");

        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        pet.setMember(member);

        return petRepository.save(pet);
    }

    // 내 반려동물 전체 조회
    public List<Pet> findAll(Integer userId) {
        log.info("PetService findAll : 내 반려동물 전체 조회");

        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        return petRepository.findByMember(member);
    }

    // 내 반려동물 상세 조회
    public Pet findById(Integer id, Integer userId) {
        log.info("PetService findById : 내 반려동물 상세 조회");

        Pet pet = petRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("해당 반려동물을 찾을 수 없습니다"));

        if (!pet.getMember().getId().equals(userId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        return pet;
    }

    // 반려동물 수정
    public Pet update(Integer id, Pet updated, Integer userId) {
        log.info("PetService update : 반려동물 수정");

        Pet pet = findById(id, userId); // 권한 체크 포함

        pet.setPhotoPath(updated.getPhotoPath());
        pet.setName(updated.getName());
        pet.setBreed(updated.getBreed());
        pet.setCoatColor(updated.getCoatColor());
        pet.setIsNeutered(updated.getIsNeutered());
        pet.setDateOfBirth(updated.getDateOfBirth());
        pet.setAge(updated.getAge());
        pet.setWeight(updated.getWeight());
        pet.setRegistrationNumber(updated.getRegistrationNumber());

        return petRepository.save(pet);
    }

    // 반려동물 삭제
    public void delete(Integer id, Integer memberId) {
        log.info("PetService delete : 반려동물 삭제");

        Pet pet = findById(id, memberId); // 권한 체크 포함
        petRepository.delete(pet);
    }

    // 반려동물의 주인 조회
    public PetResponseDto getOwner(Integer petId, Integer userId) {
        log.info("PetService getOwner : 반려동물의 주인 조회");

        Pet pet = findById(petId, userId);
        Member owner = pet.getMember();

        return new PetResponseDto(
            owner.getLoginId(),
            owner.getNickname(),
            owner.getPhone()
        );
    }

    // 동물등록번호 중복 검사
    public boolean checkRegNum(String registrationNo, Integer userId) {
        log.info("PetService checkRegNum : 동물등록번호 중복 검사");

        return petRepository.existsByRegistrationNumber(registrationNo);
    }

    // 필터링
    public List<Pet> findFiltered(Integer userId, String name, String breed) {
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));

        if (name != null && breed != null) {
            return petRepository.findByMemberAndNameContainingAndBreedContaining(member, name,
                breed);
        } else if (name != null) {
            return petRepository.findByMemberAndNameContaining(member, name);
        } else if (breed != null) {
            return petRepository.findByMemberAndBreedContaining(member, breed);
        } else {
            return petRepository.findByMember(member);
        }
    }


}
