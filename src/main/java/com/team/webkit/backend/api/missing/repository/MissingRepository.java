package com.team.webkit.backend.api.missing.repository;

import com.team.webkit.backend.api.missing.entity.Missing;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MissingRepository extends JpaRepository<Missing, Long> {

    List<Missing> findByMemberId(Integer userId);

    List<Missing> findByPetId(Integer petId);

    List<Missing> findByPostType(Missing.PostType postType);

    List<Missing> findByPetBreed(String breed);

    List<Missing> findByPetCoatColor(String coatColor);

    List<Missing> findByPetBreedAndPetCoatColor(String breed, String coatColor);

    List<Missing> findByPostTypeAndPetBreedAndPetCoatColor(String postType, String breed,
        String coatColor);

    List<Missing> findByPostTypeAndPetBreed(String postType, String breed);

    List<Missing> findByPostTypeAndPetCoatColor(String postType, String coatColor);

    List<Missing> findByPostTypeOrderByCreatedAtDesc(String postType);

    @Query("SELECT m FROM Missing m LEFT JOIN FETCH m.pet WHERE m.id IN :ids")
    List<Missing> findAllWithPetByIdIn(@Param("ids") List<Long> ids);

}
