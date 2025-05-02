package com.team.webkit.backend.api.map.Repository;

import com.team.webkit.backend.api.map.entity.MissingPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissingPostRepository extends JpaRepository<MissingPost, Long> {
    List<MissingPost> findByPostType(MissingPost.PostType postType);
    // 품종만으로 검색
    @Query("SELECT m FROM MissingPost m JOIN FETCH m.pet p WHERE p.breed = :breed")
    List<MissingPost> findByBreed(@Param("breed") String breed);

    // 털색만으로 검색
    @Query("SELECT m FROM MissingPost m JOIN FETCH m.pet p WHERE p.coatColor = :coatColor")
    List<MissingPost> findByCoatColor(@Param("coatColor") String coatColor);

    // 둘 다 검색 (이미 있음)
    @Query("SELECT m FROM MissingPost m JOIN FETCH m.pet p WHERE p.breed = :breed AND p.coatColor = :coatColor")
    List<MissingPost> findByBreedAndCoatColor(@Param("breed") String breed, @Param("coatColor") String coatColor);
}

