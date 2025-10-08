package com.example.demo.backend.analyzer.dto.SteamApi;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GameDetailDTO {
    
    Long appid;
    String name;    //유저이름
    Integer plauTimeMinutes; //플레이시간
    Integer achievementsUnlocked;   //달성한 도전 과제 수
    boolean hasPlayedRecently;  //최근 플레이 유무
}
