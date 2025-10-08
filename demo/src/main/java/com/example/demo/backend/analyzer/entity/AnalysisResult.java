package com.example.demo.backend.analyzer.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Steam 게임 라이브러리 분석 결과를 저장하는 엔티티
 * 사용자별 게임 분석 결과와 통계 정보를 관리
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "analysis_result")
public class AnalysisResult {

    /** 분석 결과 고유 ID (Primary Key) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** Steam 사용자 ID (고유값) */
    @Column(nullable = false, unique = true)
    private String steamId;

    /** 보유 게임 총 개수 */
    @Column(nullable = false)
    private Integer totalGames;

    /** 총 플레이 시간 (분 단위) */
    @Column(nullable = false)
    private Integer totalPlayTimeMinutes;

    /** 분석 결과 URL (선택사항) */
    private String analysisResultUrl;

    /** 분석 수행 일시 */
    @Column(nullable = false)
    private LocalDateTime analysisDate;

    /**
     * 게임 목록은 별도 Repository로 조회하도록 변경
     * 양방향 관계 대신 단방향으로 성능 최적화
     */
    // @OneToMany(mappedBy = "analysisResult", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<AnalyzedGame> gamesList = new ArrayList<>();

    /**
     * AnalysisResult 생성자
     * @param steamId Steam 사용자 ID
     * @param totalGames 보유 게임 총 개수
     * @param totalPlayTimeMinutes 총 플레이 시간 (분)
     * @param analysisResultUrl 분석 결과 URL
     */
    @Builder
    public AnalysisResult(String steamId, Integer totalGames, Integer totalPlayTimeMinutes, String analysisResultUrl) {
        this.steamId = steamId;
        this.totalGames = totalGames;
        this.totalPlayTimeMinutes = totalPlayTimeMinutes;
        this.analysisResultUrl = analysisResultUrl;
        this.analysisDate = LocalDateTime.now();
    }

    /**
     * 플레이 시간 업데이트
     * 분석 일시도 현재 시간으로 갱신
     * @param totalPlayTimeMinutes 새로운 총 플레이 시간
     */
    public void updatePlayTime(Integer totalPlayTimeMinutes) {
        this.totalPlayTimeMinutes = totalPlayTimeMinutes;
        this.analysisDate = LocalDateTime.now();
    }
}
