package com.team.webkit.backend.api.map.Repository;

import com.team.webkit.backend.api.missing.Missing;
import com.team.webkit.backend.api.missing.PostType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissingPostRepository extends JpaRepository<Missing, Long> {

    @Query("SELECT m FROM Missing m JOIN FETCH m.pet")
    List<Missing> findAllWithPet();

    @Query("SELECT m FROM Missing m JOIN FETCH m.pet WHERE m.postType = :postType")
    List<Missing> findByPostTypeWithPet(@Param("postType") PostType postType); // 👈 enum 타입으로 변경

    @Query("SELECT m FROM Missing m JOIN FETCH m.pet p WHERE p.breed = :breed")
    List<Missing> findByPetBreed(@Param("breed") String breed);

    @Query("SELECT m FROM Missing m JOIN FETCH m.pet p WHERE p.coatColor = :coatColor")
    List<Missing> findByPetCoatColor(@Param("coatColor") String coatColor);

    @Query("SELECT m FROM Missing m JOIN FETCH m.pet p WHERE p.breed = :breed AND p.coatColor = :coatColor")
    List<Missing> findByPetBreedAndCoatColor(@Param("breed") String breed, @Param("coatColor") String coatColor);
}
