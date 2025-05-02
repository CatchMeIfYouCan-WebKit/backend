package com.team.webkit.backend.api.pet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PetResponseDto {

    private String loginId;
    private String nickname;
    private String phone;
}
