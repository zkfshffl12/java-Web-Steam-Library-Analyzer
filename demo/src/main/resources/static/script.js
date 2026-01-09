/**
 * Steam 게임 분석기 - 클라이언트 스크립트
 * 
 * 설명:
 * - Steam OpenID 인증 흐름 처리
 * - 세션 기반 로그인 상태 관리
 * - Steam 게임 라이브러리 분석 API 호출
 * - 분석 결과 UI 렌더링
 * 
 * @author Steam Library Analyzer
 * @version 1.0
 */

// ======================================
// 1. 초기화 및 이벤트 리스너
// ======================================

/**
 * 페이지 로드 시 실행되는 초기화 함수
 */
document.addEventListener('DOMContentLoaded', function() {
    checkAuthStatus();
    checkLoginCallback();
});

// ======================================
// 2. 인증 관리 함수
// ======================================

/**
 * Steam 로그인 상태를 확인합니다.
 * 서버의 /auth/status 엔드포인트에 요청하여 현재 세션의 인증 상태를 확인합니다.
 */
function checkAuthStatus() {
    fetch('/api/v1/steam/auth/status')
        .then(response => response.json())
        .then(data => {
            if (data.authenticated && data.steamId) {
                updateUIForLoggedIn(data.steamId);
            }
        })
        .catch(error => {
            console.log('인증 상태 확인 실패:', error);
        });
}

/**
 * 로그인 콜백을 처리합니다.
 * Steam 로그인 후 리다이렉트될 때 URL 파라미터를 확인하고 분석을 시작합니다.
 */
function checkLoginCallback() {
    const urlParams = new URLSearchParams(window.location.search);
    const loginStatus = urlParams.get('login');
    const steamId = urlParams.get('steamId');
    const error = urlParams.get('error');

    if (loginStatus === 'success' && steamId) {
        console.log('✅ Steam 로그인 성공! Steam ID:', steamId);
        updateUIForLoggedIn(steamId);
        // URL 파라미터 제거 (깔끔하게)
        window.history.replaceState({}, document.title, window.location.pathname);
        // 자동으로 분석 시작
        fetchAnalysisResult(steamId);
    } else if (error) {
        displayError(error);
    }
}

/**
 * 로그인된 사용자 UI를 업데이트합니다.
 * 로그인 버튼 텍스트 변경 및 로그아웃 버튼을 추가합니다.
 * 
 * @param {string} steamId - 로그인한 Steam 사용자의 Steam ID
 */
function updateUIForLoggedIn(steamId) {
    const loginButton = document.getElementById('steam-login-button');
    loginButton.textContent = `로그인됨: ${steamId.substring(0, 8)}... | 분석 시작`;
    loginButton.onclick = () => fetchAnalysisResult(steamId);
    
    // 로그아웃 버튼 추가
    if (!document.getElementById('logout-button')) {
        const logoutButton = document.createElement('button');
        logoutButton.id = 'logout-button';
        logoutButton.textContent = '로그아웃';
        logoutButton.style.marginLeft = '10px';
        logoutButton.onclick = logout;
        loginButton.parentElement.appendChild(logoutButton);
    }
}

// 4. Steam 로그인 버튼 이벤트 핸들러
document.getElementById('steam-login-button').addEventListener('click', function () {
    // Steam OpenID 로그인 엔드포인트로 이동
    window.location.href = '/api/v1/steam/auth/login';
});

// 5. 로그아웃 함수
function logout() {
    fetch('/api/v1/steam/auth/logout', { method: 'POST' })
        .then(response => response.json())
        .then(data => {
            console.log('로그아웃 성공:', data);
            location.reload();
        })
        .catch(error => {
            console.error('로그아웃 실패:', error);
        });
}

// 6. 오류 표시
function displayError(errorCode) {
    const resultElement = document.getElementById('analysis-result');
    let errorMessage = '';
    
    switch(errorCode) {
        case 'verification_failed':
            errorMessage = '❌ Steam 인증 검증에 실패했습니다.';
            break;
        case 'no_steam_id':
            errorMessage = '❌ Steam ID를 가져올 수 없습니다.';
            break;
        case 'callback_error':
            errorMessage = '❌ 로그인 처리 중 오류가 발생했습니다.';
            break;
        default:
            errorMessage = '❌ 알 수 없는 오류가 발생했습니다.';
    }
    
    resultElement.innerHTML = `<h3>${errorMessage}</h3><p>다시 시도해주세요.</p>`;
}


// 7. 백엔드 분석 API 호출 함수
function fetchAnalysisResult(steamId) {
    const resultElement = document.getElementById('analysis-result');
    
    console.log('fetchAnalysisResult 호출됨, steamId:', steamId);
    
    resultElement.textContent = '분석 데이터를 요청 중입니다... 잠시만 기다려 주세요.';

    // 백엔드의 분석 엔드포인트를 호출합니다.
    // steamId가 있으면 포함, 없으면 서버가 세션에서 가져감
    const requestBody = steamId ? { steamId: steamId.trim() } : {};
    
    console.log('요청 본문:', requestBody);
    
    fetch('/api/v1/steam/analyzer/analyze', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody)
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
            resultElement.innerHTML = `
                <h3>❌ 분석 오류 발생</h3>
                <p><strong>오류 메시지:</strong> ${error.message}</p>
                <hr>
                <h4>🔍 문제 해결 방법:</h4>
                <ol>
                    <li><strong>Steam 프로필 공개 설정 확인</strong>
                        <ul>
                            <li>Steam → 프로필 편집 → 개인정보 설정</li>
                            <li>"내 프로필"을 <strong>공개</strong>로 변경</li>
                            <li>"게임 세부 정보"를 <strong>공개</strong>로 변경</li>
                        </ul>
                    </li>
                    <li><strong>Steam API 키 확인</strong>
                        <ul>
                            <li><a href="https://steamcommunity.com/dev/apikey" target="_blank">Steam API 키 발급</a></li>
                            <li>환경변수 STEAM_API_KEY 설정 확인</li>
                            <li>서버 재시작 필요</li>
                        </ul>
                    </li>
                    <li><strong>Steam ID 형식 확인</strong>
                        <ul>
                            <li>17자리 숫자여야 함 (예: 76561198012345678)</li>
                            <li><a href="https://steamid.io/" target="_blank">Steam ID 변환 도구</a></li>
                        </ul>
                    </li>
                    <li><strong>서버 로그 확인</strong>
                        <ul>
                            <li>브라우저 개발자 도구 (F12) → Console 탭</li>
                            <li>서버 터미널에서 상세 오류 메시지 확인</li>
                        </ul>
                    </li>
                </ol>
                <p><em>자세한 설정 방법은 <a href="/STEAM_API_SETUP.md" target="_blank">STEAM_API_SETUP.md</a> 파일을 참고하세요.</em></p>
            `;
        });
}