package com.example.demo.backend.analyzer.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "analyzed_game")
public class AnalyzedGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String appId;

    @Column(nullable = false)
    private String gameName;

    @Column(nullable = false)
    private Integer playTimeMinutes;

    @Column(name = "analysis_result_id")
    private Long analysisResultId;

    @Builder
    public AnalyzedGame(String appId, String gameName, Integer playTimeMinutes, Long analysisResultId) {
        this.appId = appId;
        this.gameName = gameName;
        this.playTimeMinutes = playTimeMinutes;
        this.analysisResultId = analysisResultId;
    }
}