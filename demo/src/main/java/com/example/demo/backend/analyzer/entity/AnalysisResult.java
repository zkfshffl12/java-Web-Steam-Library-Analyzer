package com.example.demo.backend.analyzer.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "analysis_result")
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String steamId;

    @Column(nullable = false)
    private Integer totalgames;

    @Column(nullable = false)
    private Integer totalPlayTimeMinutes;

    private String analysisResultUrl;

    @Column(nullable = false)
    private LocalDateTime analysisDate;

    // 게임 목록은 별도 Repository로 조회하도록 변경
    // @OneToMany(mappedBy = "analysisResult", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<AnalyzedGame> gamesList=new ArrayList<>();

    @Builder
    public AnalysisResult(String steamId, Integer totalGames, Integer totalPlayTimeMinutes, String analysisResultUrl) {
        this.steamId=steamId;
        this.totalgames=totalGames;
        this.totalPlayTimeMinutes=totalPlayTimeMinutes;
        this.analysisResultUrl=analysisResultUrl;
        this.analysisDate=LocalDateTime.now();
        
    }

    public void updatePlayTime(Integer totalPlayTimeMinutes){
        this.totalPlayTimeMinutes=totalPlayTimeMinutes;
        this.analysisDate=LocalDateTime.now();
    }

}
