package com.team.webkit.backend.api.adopt.dto;

import com.team.webkit.backend.api.pet.entity.Pet;
import lombok.Data;

@Data
public class PetSimpleDto {
    private Integer id;
    private String name;
    private String breed;
    private String gender;
    private String coatColor;
    private String photoPath;

    public static PetSimpleDto from(Pet pet) {
        PetSimpleDto dto = new PetSimpleDto();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setBreed(pet.getBreed());
        dto.setGender(pet.getGender());
        dto.setCoatColor(pet.getCoatColor());
        dto.setPhotoPath(pet.getPhotoPath());
        return dto;
    }
}
