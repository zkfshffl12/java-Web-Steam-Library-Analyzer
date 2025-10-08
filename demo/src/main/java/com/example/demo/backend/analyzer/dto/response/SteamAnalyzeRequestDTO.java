package com.example.demo.backend.analyzer.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.backend.analyzer.dto.SteamApi.GameDetailDTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SteamAnalyzeRequestDTO {
    
    String steamId;
    Integer totaInteger;    // 라이브러리에 있는 전체 게임 수
    Integer totalPlayTimeHours;// 모든 게임의 총 플레이 시간 (시간 단위)

    List<GameDetailDTO> gamesList;

    LocalDateTime analysisdate; // 분석이 수행된 서버 시간
    String analysisResultUrl;   // 분석 결과 페이지의 고유 URL (퍼머링크)

}
