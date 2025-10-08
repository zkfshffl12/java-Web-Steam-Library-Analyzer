package com.example.demo.backend.analyzer.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class SteamRequestDTO {
    
    @NotBlank(message = "Steam ID는 필수입니다.")
    @Size(min = 3, max = 50, message = "ID는 3자에서 50자 사이여야 합니다.")
    String steamId;

    boolean ispublic;
}
