package com.example.demo.backend.analyzer.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * Steam 게임 분석 요청 DTO
 * 클라이언트로부터 받는 Steam 사용자 정보 요청 데이터
 */
@Value
@AllArgsConstructor
public class SteamRequestDTO {
    
    /** 
     * Steam 사용자 ID 또는 사용자명
     * 필수 입력값이며 3-50자 사이여야 함
     */
    @NotBlank(message = "Steam ID는 필수입니다.")
    @Size(min = 3, max = 50, message = "ID는 3자에서 50자 사이여야 합니다.")
    String steamId;

    /** 
     * Steam 프로필 공개 여부
     * true: 공개 프로필, false: 비공개 프로필
     */
    boolean isPublic;
}
