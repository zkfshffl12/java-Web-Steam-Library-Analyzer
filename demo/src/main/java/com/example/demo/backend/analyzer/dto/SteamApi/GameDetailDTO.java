package com.example.demo.backend.analyzer.dto.SteamApi;

import lombok.Builder;
import lombok.Value;

/**
 * Steam API에서 받아오는 게임 상세 정보 DTO
 * Steam Web API의 GetOwnedGames 응답 데이터를 매핑하는 클래스
 */
@Value
@Builder
public class GameDetailDTO {

    /** Steam 게임 고유 ID (Application ID) */
    Long appid;

    /** 게임 이름 */
    String name;

    /** 플레이 시간 (분 단위) */
    Integer playTimeMinutes;

    /** 달성한 도전 과제 수 */
    Integer achievementsUnlocked;

    /** 최근 플레이 여부 (2주 이내) */
    boolean hasPlayedRecently;
}
