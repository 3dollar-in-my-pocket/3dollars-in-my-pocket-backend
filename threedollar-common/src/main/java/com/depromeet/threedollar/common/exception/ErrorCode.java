package com.depromeet.threedollar.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.depromeet.threedollar.common.exception.ErrorAlarmOptions.*;
import static com.depromeet.threedollar.common.exception.ErrorStatusCode.*;
import static com.depromeet.threedollar.common.exception.ErrorStatusCode.BAD_REQUEST;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // Common
    VALIDATION_EXCEPTION(BAD_REQUEST, OFF, "C001", "잘못된 요청입니다"),
    UNAUTHORIZED_EXCEPTION(UNAUTHORIZED, OFF, "C002", "세션이 만료되었습니다. 다시 로그인 해주세요"),
    FORBIDDEN_EXCEPTION(FORBIDDEN, OFF, "C003", "허용하지 않는 요청입니다."),
    NOT_FOUND_EXCEPTION(NOT_FOUND, OFF, "C004", "존재하지 않습니다"),
    CONFLICT_EXCEPTION(CONFLICT, OFF, "C005", "이미 존재합니다"),
    INTERNAL_SERVER_EXCEPTION(INTERNAL_SERVER, ON, "C006", "서버 내부에서 에러가 발생하였습니다"),
    METHOD_NOT_ALLOWED_EXCEPTION(METHOD_NOT_ALLOWED, OFF, "C007", "지원하지 않는 메소드 입니다"),
    BAD_GATEWAY_EXCEPTION(BAD_GATEWAY, ON, "C008", "외부 연동 중 에러가 발생하였습니다"),
    SERVICE_UNAVAILABLE_EXCEPTION(SERVICE_UNAVAILABLE, OFF, "C009", "서비스를 이용하실 수 없습니다"),
    UNSUPPORTED_MEDIA_TYPE(ErrorStatusCode.UNSUPPORTED_MEDIA_TYPE, OFF, "C010", "해당하는 미디어 타입을 지원하지 않습니다"),

    // 400 Bad Request
    VALIDATION_RATING_EXCEPTION(BAD_REQUEST, OFF, "V001", "허용되지 않은 평가 점수입니다. (0 ~ 5)"),
    VALIDATION_LATITUDE_EXCEPTION(BAD_REQUEST, OFF, "V002", "허용되지 않은 위도 범위를 입력하였습니다. (33 ~ 43)"),
    VALIDATION_LONGITUDE_EXCEPTION(BAD_REQUEST, OFF, "V003", "허용되지 않은 경도 범위를 입력하였습니다. (124 ~ 132)"),
    VALIDATION_FILE_TYPE_EXCEPTION(BAD_REQUEST, OFF, "V004", "허용되지 않은 파일 형식입니다"),
    VALIDATION_APPLE_TOKEN_EXCEPTION(BAD_REQUEST, OFF, "V005", "잘못된 애플 토큰입니다"),
    VALIDATION_APPLE_TOKEN_EXPIRED_EXCEPTION(BAD_REQUEST, OFF, "V006", "만료된 애플 토큰입니다"),
    VALIDATION_SOCIAL_TYPE_EXCEPTION(BAD_REQUEST, OFF, "V007", "잘못된 소셜 타입 입니다."),
    VALIDATION_FILE_UPLOAD_EXCEPTION(BAD_REQUEST, OFF, "V008", "파일 업로드 중 에러가 발생하였습니다"),

    // 404 Not Found
    NOT_FOUND_USER_EXCEPTION(NOT_FOUND, OFF, "N001", "존재하지 않는 유저입니다"),
    NOT_FOUND_STORE_EXCEPTION(NOT_FOUND, OFF, "N002", "존재하지 않는 가게입니다"),
    NOT_FOUND_REVIEW_EXCEPTION(NOT_FOUND, OFF, "N003", "존재하지 않는 리뷰입니다"),
    NOT_FOUND_STORE_IMAGE_EXCEPTION(NOT_FOUND, OFF, "N003", "존재하지 않는 가게 이미지입니다"),
    NOT_FOUND_FAQ_EXCEPTION(NOT_FOUND, OFF, "N004", "존재하지 않는 FAQ입니다"),
    ;

    private final ErrorStatusCode statusCode;
    private final ErrorAlarmOptions alarmOptions;
    private final String code;
    private final String message;

    public int getStatus() {
        return statusCode.getStatus();
    }

    public boolean isSetAlarm() {
        return this.alarmOptions.isSetAlarm();
    }

}
