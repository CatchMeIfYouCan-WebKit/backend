package com.team.webkit.backend.api.adopt.repository;

import com.team.webkit.backend.api.adopt.entity.AdoptPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptPostRepository extends JpaRepository<AdoptPost, Long> {
    @Query("""
    SELECT a FROM AdoptPost a
    WHERE (:region IS NULL OR a.adoptLocation LIKE %:region%)
      AND (:age IS NULL OR a.age = :age)
      AND (:breed IS NULL OR a.breed = :breed)
      AND (:color IS NULL OR a.coatColor = :color)
    """)
    List<AdoptPost> findByFilter(
            @Param("region") String region,
            @Param("age") Integer age,
            @Param("breed") String breed,
            @Param("color") String color
    );
}
