package com.example.demo.backend.analyzer.service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.demo.backend.analyzer.dto.SteamApi.GameDetailDTO;
import com.example.demo.backend.analyzer.dto.SteamApi.SteamApiResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
@Service
public class SteamApiService {
    
    private final WebClient webClient;
    private final String steamApiKey;

    public SteamApiService(
        WebClient.Builder webClientBuilder,
        @Value("${steam.api.base-url}") String baseUrl,
        @Value("${steam.api.key}") String steamApiKey
    ) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.steamApiKey = steamApiKey;
    }

    /**
     * Steam API를 호출하여 사용자의 소유 게임 목록을 조회합니다.
     * @param steamId Steam 사용자 ID
     * @return 게임 목록
     */
    public List<GameDetailDTO> getOwnedGames(String steamId) {
        log.info("Steam API 호출 시작, Steam ID: {}", steamId);

        String uri = "/IPlayerService/GetOwnedGames/v0001/" +
                     "?key={key}&steamid={steamid}&format=json&include_appinfo=1&include_played_free_games=1";

        try {
            // Steam API 호출
            Mono<SteamApiResponse> responseMono = webClient.get()
                    .uri(uri, steamApiKey, steamId)
                    .retrieve()
                    .bodyToMono(SteamApiResponse.class)
                    .timeout(Duration.ofSeconds(10))
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                            .filter(throwable -> !(throwable instanceof WebClientResponseException.BadRequest)));

            SteamApiResponse response = responseMono.block();

            if (response != null && response.getResponse() != null && response.getResponse().getGames() != null) {
                List<GameDetailDTO> games = convertToGameDetailDTOs(response.getResponse().getGames());
                log.info("Steam API 호출 성공. Steam ID: {}, 게임 수: {}", steamId, games.size());
                return games;
            } else {
                log.warn("Steam API 응답이 비어있습니다. Steam ID: {}", steamId);
                return Collections.emptyList();
            }

        } catch (WebClientResponseException.Forbidden e) {
            log.error("Steam API 접근 권한 없음 (프로필 비공개). Steam ID: {}", steamId);
            return Collections.emptyList();
        } catch (WebClientResponseException.Unauthorized e) {
            log.error("Steam API 키가 유효하지 않습니다. Steam ID: {}", steamId);
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Steam API 호출 중 오류 발생. Steam ID: {}", steamId, e);
            // 오류 발생 시 Mock 데이터 반환 (개발용)
            log.info("Mock 데이터로 대체합니다.");
            return createMockGameData();
        }
    }

    /**
     * Steam API 응답을 GameDetailDTO로 변환
     * @param steamGames Steam API 게임 목록
     * @return GameDetailDTO 목록
     */
    private List<GameDetailDTO> convertToGameDetailDTOs(List<SteamApiResponse.SteamGame> steamGames) {
        return steamGames.stream()
                .map(steamGame -> GameDetailDTO.builder()
                        .appid(steamGame.getAppId())
                        .name(steamGame.getName())
                        .playTimeMinutes(steamGame.getPlaytimeForever() != null ? steamGame.getPlaytimeForever() : 0)
                        .achievementsUnlocked(0) // Steam API에서 제공하지 않음, 별도 API 필요
                        .hasPlayedRecently(steamGame.getPlaytime2weeks() != null && steamGame.getPlaytime2weeks() > 0)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 테스트용 Mock 게임 데이터 생성
     */
    private List<GameDetailDTO> createMockGameData() {
        return List.of(
            GameDetailDTO.builder()
                .appid(730L)
                .name("Counter-Strike 2")
                .playTimeMinutes(1200)
                .achievementsUnlocked(15)
                .hasPlayedRecently(true)
                .build(),
            GameDetailDTO.builder()
                .appid(570L)
                .name("Dota 2")
                .playTimeMinutes(800)
                .achievementsUnlocked(8)
                .hasPlayedRecently(false)
                .build()
        );
    }
}
