package com.example.demo.backend.analyzer.dto.SteamApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Steam API GetOwnedGames 응답 DTO
 */
@Data
public class SteamApiResponse {
    
    @JsonProperty("response")
    private ResponseData response;
    
    @Data
    public static class ResponseData {
        
        @JsonProperty("game_count")
        private Integer gameCount;
        
        @JsonProperty("games")
        private List<SteamGame> games;
    }
    
    @Data
    public static class SteamGame {
        
        @JsonProperty("appid")
        private Long appId;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("playtime_forever")
        private Integer playtimeForever; // 총 플레이 시간 (분)
        
        @JsonProperty("playtime_2weeks")
        private Integer playtime2weeks; // 최근 2주 플레이 시간 (분)
        
        @JsonProperty("img_icon_url")
        private String imgIconUrl;
        
        @JsonProperty("img_logo_url")
        private String imgLogoUrl;
        
        @JsonProperty("has_community_visible_stats")
        private Boolean hasCommunityVisibleStats;
    }
}