package com.example.demo.backend.analyzer.service;

import com.example.demo.backend.analyzer.dto.response.SteamAnalyzeRequestDTO;
import com.example.demo.backend.analyzer.dto.SteamApi.GameDetailDTO;
import com.example.demo.backend.analyzer.config.SteamApiConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Steam 게임 라이브러리 분석 서비스
 * Steam API 호출, 데이터 분석, 결과 저장을 담당
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SteamAnalyzerService {

    private final SteamApiConfig steamApiConfig;
    private final SteamApiService steamApiService;

    /**
     * Steam ID를 분석하고 결과를 반환합니다.
     * 
     * @param requestDTO Steam 분석 요청 DTO
     * @return 분석 결과 DTO
     */
    @Transactional
    public SteamAnalyzeRequestDTO analyze(SteamAnalyzeRequestDTO requestDTO) {
        String steamId = requestDTO.getSteamId();
        log.info("Steam ID {}에 대한 라이브러리 분석을 시작합니다.", steamId);
        String apiKey = steamApiConfig.getKey();
        if (apiKey != null && apiKey.length() > 8) {
            log.info("Steam API 키: {}...", apiKey.substring(0, 8));
        } else {
            log.warn("Steam API 키가 설정되지 않았거나 너무 짧습니다: {}", apiKey);
        }
        log.info("Steam API URL: {}", steamApiConfig.getOwnedGamesUrl());

        // 1. Steam API를 통해 게임 데이터 조회
        List<GameDetailDTO> gameData = steamApiService.getOwnedGames(steamId);

        // 2. 비즈니스 로직: 전체 플레이 시간 및 게임 수 계산
        int totalMinutes = gameData.stream()
                .mapToInt(GameDetailDTO::getPlayTimeMinutes)
                .sum();

        // 3. 분석 결과 생성
        SteamAnalyzeRequestDTO response = SteamAnalyzeRequestDTO.builder()
                .steamId(steamId)
                .totalGames(gameData.size())
                .totalPlayTimeHours(totalMinutes / 60)
                .gamesList(gameData)
                .analysisDate(LocalDateTime.now())
                .analysisResultUrl("http://localhost:9000/api/v1/steam/analyzer/result/" + steamId)
                .build();

        log.info("Steam ID {}에 대한 라이브러리 분석을 완료했습니다.", steamId);
        return response;
    }

    /**
     * 분석 결과 ID로 결과를 조회합니다.
     * 
     * @param resultId 분석 결과 ID
     * @return 분석 결과 DTO
     */
    @Transactional(readOnly = true)
    public SteamAnalyzeRequestDTO getResultById(Long resultId) {
        log.info("분석 결과 조회 요청: resultId = {}", resultId);

        // TODO: 실제 DB 조회 로직 구현
        // 현재는 Mock 데이터 반환
        List<GameDetailDTO> mockData = steamApiService.getOwnedGames("mock_steam_id");

        return SteamAnalyzeRequestDTO.builder()
                .steamId("mock_steam_id")
                .totalGames(mockData.size())
                .totalPlayTimeHours(33)
                .gamesList(mockData)
                .analysisDate(LocalDateTime.now())
                .analysisResultUrl("http://localhost:9000/api/v1/steam/analyzer/result/" + resultId)
                .build();
    }
}
