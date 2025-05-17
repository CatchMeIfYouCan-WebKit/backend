package com.team.webkit.backend.api.ai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_predictions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_type", nullable = false)
    private String postType; // 'missing' 또는 'witness'

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "predicted_breed")
    private String predictedBreed;

    @Column(name = "predicted_color")
    private String predictedColor;

    @Column(name = "breed_score")
    private Float breedScore;

    @Column(name = "color_score")
    private Float colorScore;

    @Column(name = "model_version")
    private String modelVersion;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

}

