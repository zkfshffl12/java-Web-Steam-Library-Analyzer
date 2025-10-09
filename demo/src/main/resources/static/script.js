// script.js

// 1. Steam 로그인 버튼 이벤트 핸들러
document.getElementById('steam-login-button').addEventListener('click', function () {
    // ⚠️ 실제 구현에서는 이 부분이 백엔드의 Steam OpenID 인증 시작 API를 호출해야 합니다.
    // 예시: 백엔드의 인증 시작 엔드포인트로 이동
    // window.location.href = '/api/v1/steam/auth/login'; 

    alert('Steam 로그인 시뮬레이션: 실제로는 Steam 인증 페이지로 이동합니다.');

    // 임시로, Steam ID를 가정하고 분석 API를 호출하는 버튼을 활성화합니다.
    document.getElementById('steam-login-button').textContent = "분석 시작 (임시 Steam ID 사용)";
    document.getElementById('steam-login-button').removeEventListener('click', arguments.callee); // 이벤트 제거
    document.getElementById('steam-login-button').addEventListener('click', fetchAnalysisResult);
});


// 2. 백엔드 분석 API 호출 함수
function fetchAnalysisResult() {
    // ⚠️ 실제로는 인증 후 백엔드가 획득한 Steam ID를 사용해야 합니다.
    // 여기서는 테스트를 위해 임시 Steam ID를 사용합니다. (예: 76561198037355291 - 유명 Steam ID 중 하나)
    const TEMP_STEAM_ID = '76561198037355291';

    const resultElement = document.getElementById('analysis-result');
    resultElement.textContent = '분석 데이터를 요청 중입니다... 잠시만 기다려 주세요.';

    // 백엔드의 분석 엔드포인트를 호출합니다.
    fetch('/api/v1/steam/analyzer/analyze', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            steamId: TEMP_STEAM_ID,
            isPublic: true
        })
    })
        .then(response => {
            if (!response.ok) {
                // 4xx 또는 5xx 오류 처리
                return response.json().then(errorData => {
                    throw new Error(errorData.message || `HTTP error! Status: ${response.status}`);
                });
            }
            return response.json();
        })
        .then(data => {
            // JSON 데이터를 보기 좋게 표시
            resultElement.innerHTML = `
                <h3>✅ 분석 완료!</h3>
                <p><strong>Steam ID:</strong> ${data.steamId}</p>
                <p><strong>총 게임 수:</strong> ${data.totalGames}개</p>
                <p><strong>총 플레이 시간:</strong> ${data.totalPlayTimeHours}시간</p>
                <p><strong>분석 일시:</strong> ${new Date(data.analysisDate).toLocaleString()}</p>
                <h4>게임 목록:</h4>
                <ul>
                    ${data.gamesList.map(game =>
                `<li>${game.name} - ${Math.floor(game.playTimeMinutes / 60)}시간</li>`
            ).join('')}
                </ul>
            `;
        })
        .catch(error => {
            console.error('분석 중 오류 발생:', error);
            resultElement.textContent = `❌ 분석 오류 발생: ${error.message}\n백엔드 서버 및 API 키 설정을 확인하세요.`;
        });
}