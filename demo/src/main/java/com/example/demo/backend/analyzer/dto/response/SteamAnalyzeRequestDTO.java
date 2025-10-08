package com.example.demo.backend.analyzer.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.backend.analyzer.dto.SteamApi.GameDetailDTO;

import lombok.Builder;
import lombok.Value;

/**
 * Steam 게임 라이브러리 분석 결과 응답 DTO
 * 클라이언트에게 전달할 분석 완료된 데이터를 담는 클래스
 */
@Value
@Builder
public class SteamAnalyzeRequestDTO {
    
    /** Steam 사용자 ID */
    String steamId;
    
    /** 라이브러리에 있는 전체 게임 수 */
    Integer totalGames;
    
    /** 모든 게임의 총 플레이 시간 (시간 단위) */
    Integer totalPlayTimeHours;

    /** 분석된 게임 목록 (상세 정보 포함) */
    List<GameDetailDTO> gamesList;

    /** 분석이 수행된 서버 시간 */
    LocalDateTime analysisDate;
    
    /** 분석 결과 페이지의 고유 URL (퍼머링크) */
    String analysisResultUrl;
}
