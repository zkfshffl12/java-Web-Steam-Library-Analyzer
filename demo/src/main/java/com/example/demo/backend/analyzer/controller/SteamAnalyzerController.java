package com.example.demo.backend.analyzer.controller;

import com.example.demo.backend.analyzer.dto.response.SteamAnalyzeRequestDTO;
import com.example.demo.backend.analyzer.service.SteamAnalyzerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Steam 게임 라이브러리 분석 API 컨트롤러
 * Steam 사용자의 게임 데이터를 분석하고 결과를 제공하는 REST API
 */
@RestController
@RequestMapping("/api/v1/steam/analyzer")
@RequiredArgsConstructor
@Slf4j
public class SteamAnalyzerController {

    private final SteamAnalyzerService steamAnalyzerService;

    /**
     * 헬스 체크용 간단한 GET 엔드포인트
     * URI: GET /api/v1/steam/analyzer/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("헬스 체크 요청");
        return ResponseEntity.ok("Steam Analyzer API is running!");
    }

    /**
     * 루트 경로 테스트용 엔드포인트
     * URI: GET /api/v1/steam/analyzer
     */
    @GetMapping
    public ResponseEntity<String> root() {
        log.info("루트 경로 요청");
        return ResponseEntity.ok("Welcome to Steam Analyzer API");
    }

    /**
     * POST 요청: Steam ID를 받아 라이브러리 분석을 시작합니다.
     * URI: POST /api/v1/steam/analyzer/analyze
     */
    @PostMapping("/analyze")
    public ResponseEntity<SteamAnalyzeRequestDTO> analyzeLibrary(
            @RequestBody @Valid SteamAnalyzeRequestDTO requestDTO) {
        
        try {
            log.info("분석 요청 받음: steamId = {}", requestDTO.getSteamId());
            
            // 서비스 계층에 분석 요청 로직 위임
            SteamAnalyzeRequestDTO response = steamAnalyzerService.analyze(requestDTO);
            
            log.info("분석 완료: steamId = {}", requestDTO.getSteamId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            log.error("분석 중 오류 발생: steamId = {}, error = {}", 
                     requestDTO.getSteamId(), e.getMessage(), e);
            throw e; // GlobalExceptionHandler가 처리하도록 다시 던짐
        }
    }

    /**
     * GET 요청: 특정 분석 ID의 결과를 조회합니다.
     * URI: GET /api/v1/steam/analyzer/{resultId}
     */
    @GetMapping("/{resultId}")
    public ResponseEntity<SteamAnalyzeRequestDTO> getAnalysisResult(@PathVariable Long resultId) {
        
        try {
            log.info("분석 결과 조회 요청: resultId = {}", resultId);
            
            // 서비스 계층에 결과 조회 로직 위임
            SteamAnalyzeRequestDTO response = steamAnalyzerService.getResultById(resultId);
            
            log.info("분석 결과 조회 완료: resultId = {}", resultId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("분석 결과 조회 중 오류 발생: resultId = {}, error = {}", 
                     resultId, e.getMessage(), e);
            throw e; // GlobalExceptionHandler가 처리하도록 다시 던짐
        }
    }
}