package com.example.demo.backend.analyzer.exception;

/**
 * Steam API 호출 관련 예외 클래스
 */
public class SteamApiException extends RuntimeException {

    public SteamApiException(String message) {
        super(message);
    }

    public SteamApiException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Steam API 키가 유효하지 않을 때 발생하는 예외
     */
    public static class InvalidApiKeyException extends SteamApiException {
        public InvalidApiKeyException() {
            super("Steam API 키가 유효하지 않습니다.");
        }
    }

    /**
     * Steam 프로필이 비공개일 때 발생하는 예외
     */
    public static class PrivateProfileException extends SteamApiException {
        public PrivateProfileException(String steamId) {
            super("Steam 프로필이 비공개입니다. Steam ID: " + steamId);
        }
    }

    /**
     * Steam 사용자를 찾을 수 없을 때 발생하는 예외
     */
    public static class UserNotFoundException extends SteamApiException {
        public UserNotFoundException(String steamId) {
            super("Steam 사용자를 찾을 수 없습니다. Steam ID: " + steamId);
        }
    }

    /**
     * Steam API 호출 한도 초과 시 발생하는 예외
     */
    public static class RateLimitExceededException extends SteamApiException {
        public RateLimitExceededException() {
            super("Steam API 호출 한도를 초과했습니다. 잠시 후 다시 시도해주세요.");
        }
    }
}