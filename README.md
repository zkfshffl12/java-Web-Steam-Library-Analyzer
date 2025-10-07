# Steam Game Analyzer

Steam 게임 분석 프로젝트

## 프로젝트 구조

### Backend (Spring Boot)
```
demo/src/main/java/com/steam/analyzer/
├── config/          # 환경 설정 및 빈(Bean) 정의
├── controller/      # 클라이언트 요청 처리 및 응답 반환
├── dto/            # 데이터 전송 객체 (In/Out)
│   ├── SteamApi/   # Steam API 응답 전용 DTO
│   └── response/   # 클라이언트에게 반환할 최종 DTO
├── entity/         # 데이터베이스 테이블과 매핑
├── repository/     # DB 통신
├── service/        # 핵심 비즈니스 로직 및 외부 통신
└── exception/      # 전역 예외 처리
```

### Frontend (React + TypeScript)
```
frontend/
├── src/
│   ├── components/  # 재사용 가능한 컴포넌트
│   ├── pages/      # 페이지 컴포넌트
│   ├── services/   # API 호출 관련
│   ├── types/      # TypeScript 타입 정의
│   ├── hooks/      # 커스텀 훅
│   ├── utils/      # 유틸리티 함수
│   └── styles/     # CSS/스타일 파일
└── public/         # 정적 파일
```

## 기능

- Steam API를 통한 게임 정보 조회
- 사용자 소유 게임 목록 분석
- Redis 캐싱을 통한 성능 최적화