package com.example.demo.backend.analyzer.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 분석된 개별 게임 정보를 저장하는 엔티티
 * AnalysisResult와 연관되어 사용자별 게임 상세 데이터를 관리
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "analyzed_game")
public class AnalyzedGame {

    /** 분석된 게임 고유 ID (Primary Key) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Steam 게임 고유 ID (Application ID) */
    @Column(nullable = false)
    private String appId;

    /** 게임 이름 */
    @Column(nullable = false)
    private String gameName;

    /** 플레이 시간 (분 단위) */
    @Column(nullable = false)
    private Integer playTimeMinutes;

    /** 연관된 분석 결과 ID (Foreign Key) */
    @Column(name = "analysis_result_id")
    private Long analysisResultId;

    /**
     * AnalyzedGame 생성자
     * @param appId Steam 게임 고유 ID
     * @param gameName 게임 이름
     * @param playTimeMinutes 플레이 시간 (분)
     * @param analysisResultId 연관된 분석 결과 ID
     */
    @Builder
    public AnalyzedGame(String appId, String gameName, Integer playTimeMinutes, Long analysisResultId) {
        this.appId = appId;
        this.gameName = gameName;
        this.playTimeMinutes = playTimeMinutes;
        this.analysisResultId = analysisResultId;
    }
}