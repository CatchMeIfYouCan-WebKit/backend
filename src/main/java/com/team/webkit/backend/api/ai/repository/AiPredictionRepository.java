package com.team.webkit.backend.api.ai.repository;

import com.team.webkit.backend.api.ai.entity.AiPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AiPredictionRepository extends JpaRepository<AiPrediction, Long> {

    @Query("""
        SELECT a.postId
        FROM AiPrediction a
        WHERE a.postType = 'witness'
          AND a.predictedBreed = :breed
          AND a.predictedColor = :coatColor
          AND a.createdAt >= :cutoff
    """)
    List<Long> findMatchingWitnessPostIds(
            @Param("breed") String breed,
            @Param("coatColor") String coatColor,
            @Param("cutoff") LocalDateTime cutoff
    );

    List<AiPrediction> findAllByPostIdIn(List<Long> postIds);

    // 🔹 1. 특정 게시글 ID에 해당하는 AI 예측 정보 조회
    Optional<AiPrediction> findByPostId(Long postId);

    // 🔹 2. 목격글 예측과 유사한 실종글 postId 조회
    @Query("SELECT a.postId FROM AiPrediction a " +
            "WHERE a.postType = 'missing' " +
            "AND a.predictedBreed = :breed " +
            "AND a.predictedColor = :color " +
            "AND a.createdAt >= :cutoff")
    List<Long> findMatchingMissingPostIds(String breed, String color, LocalDateTime cutoff);
}
