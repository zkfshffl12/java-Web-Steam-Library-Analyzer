package com.example.demo.backend.analyzer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Steam API 설정 클래스
 * application.yml의 steam.api 설정을 바인딩
 */
@Configuration
@ConfigurationProperties(prefix = "steam.api")
@Getter
@Setter
public class SteamApiConfig {

    /** Steam API 키 */
    private String key;

    /** Steam API 기본 URL */
    private String baseUrl;

    /** Steam API 엔드포인트들 */
    private Endpoints endpoints = new Endpoints();

    @Getter
    @Setter
    public static class Endpoints {
        /** 소유 게임 목록 조회 엔드포인트 */
        private String ownedGames;
        
        /** 플레이어 요약 정보 엔드포인트 */
        private String playerSummaries;
        
        /** 사용자 게임 통계 엔드포인트 */
        private String userStats;
    }

    /**
     * 완전한 API URL 생성
     * @param endpoint 엔드포인트 경로
     * @return 완전한 URL
     */
    public String getFullUrl(String endpoint) {
        return baseUrl + endpoint;
    }

    /**
     * 소유 게임 목록 조회 URL 생성
     * @return 소유 게임 API URL
     */
    public String getOwnedGamesUrl() {
        return getFullUrl(endpoints.getOwnedGames());
    }

    /**
     * 플레이어 요약 정보 URL 생성
     * @return 플레이어 요약 API URL
     */
    public String getPlayerSummariesUrl() {
        return getFullUrl(endpoints.getPlayerSummaries());
    }
}