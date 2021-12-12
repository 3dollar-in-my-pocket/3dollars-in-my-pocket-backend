package com.depromeet.threedollar.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.depromeet.threedollar.common.exception.ErrorAlarmOptionType.*;
import static com.depromeet.threedollar.common.exception.HttpStatusCode.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // 400 Bad Request
    VALIDATION_EXCEPTION(BAD_REQUEST, OFF, "BR001", "잘못된 요청입니다"),
    VALIDATION_RATING_EXCEPTION(BAD_REQUEST, OFF, "BR002", "허용되지 않은 평가 점수입니다. (1 ~ 5)"), // TODO 403에러로 변경
    VALIDATION_LATITUDE_EXCEPTION(BAD_REQUEST, OFF, "BR003", "허용되지 않은 위도 범위를 입력하였습니다. (33.1 ~ 38.61)"), // TODO 차후 403에러로 변경
    VALIDATION_LONGITUDE_EXCEPTION(BAD_REQUEST, OFF, "BR004", "허용되지 않은 경도 범위를 입력하였습니다. (124.60 ~ 131.87)"), // TODO 차후 403에러로 변경
    VALIDATION_FILE_NAME_EXCEPTION(BAD_REQUEST, OFF, "BR005", "허용되지 않은 파일 이름입니다"), // TODO 차후 403에러로 변경
    VALIDATION_FILE_TYPE_EXCEPTION(BAD_REQUEST, OFF, "BR006", "허용되지 않은 파일 형식입니다"), // TODO 차후 403에러로 변경
    VALIDATION_APPLE_TOKEN_EXCEPTION(BAD_REQUEST, OFF, "BR007", "잘못된 애플 토큰입니다"),
    VALIDATION_APPLE_TOKEN_EXPIRED_EXCEPTION(BAD_REQUEST, OFF, "BR008", "만료된 애플 토큰입니다."),
    VALIDATION_SOCIAL_TYPE_EXCEPTION(BAD_REQUEST, OFF, "BR009", "잘못된 소셜 프로바이더 입니다."),
    VALIDATION_REQUEST_MISSING_EXCEPTION(BAD_REQUEST, OFF, "BR010", "필수적인 요청 값이 입력되지 않았습니다"),
    VALIDATION_WRONG_TYPE_EXCEPTION(BAD_REQUEST, OFF, "BR011", "잘못된 타입이 입력되었습니다."),
    VALIDATION_UPLOAD_SIZE_EXCEPTION(BAD_REQUEST, ON, "BR012", "업로드 가능한 최대 파일의 크기를 초과했습니다"), // TODO 차후 403에러로 변경

    // 401 UnAuthorized
    UNAUTHORIZED_EXCEPTION(UNAUTHORIZED, OFF, "UA001", "세션이 만료되었습니다. 다시 로그인 해주세요"),

    // 403 Forbidden
    FORBIDDEN_EXCEPTION(FORBIDDEN, OFF, "FB001", "허용하지 않는 요청입니다."),

    // 404 Not Found
    NOT_FOUND_EXCEPTION(NOT_FOUND, OFF, "NF001", "존재하지 않습니다"),
    NOT_FOUND_USER_EXCEPTION(NOT_FOUND, OFF, "NF002", "탈퇴하거나 존재하지 않는 유저입니다"),
    NOT_FOUND_STORE_EXCEPTION(NOT_FOUND, OFF, "NF003", "삭제되거나 존재하지 않는 가게입니다"),
    NOT_FOUND_REVIEW_EXCEPTION(NOT_FOUND, OFF, "NF004", "삭제되거나 존재하지 않는 리뷰입니다"),
    NOT_FOUND_STORE_IMAGE_EXCEPTION(NOT_FOUND, OFF, "NF005", "삭제되거나 존재하지 않는 가게 이미지입니다"),
    NOT_FOUND_FAQ_EXCEPTION(NOT_FOUND, OFF, "NF006", "삭제되거나 존재하지 않는 FAQ입니다"),
    NOT_FOUND_MEDAL_EXCEPTION(NOT_FOUND, OFF, "N007", "보유하지 않는 메달입니다"),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED_EXCEPTION(METHOD_NOT_ALLOWED, OFF, "MN001", "지원하지 않는 메소드 입니다"),

    // 406 Not Acceptable
    NOT_ACCEPTABLE_EXCEPTION(NOT_ACCEPTABLE, OFF, "NA001", "Not Acceptable"),

    // 409 Conflict
    CONFLICT_EXCEPTION(CONFLICT, OFF, "CF001", "이미 존재합니다"),
    CONFLICT_NICKNAME_EXCEPTION(CONFLICT, OFF, "CF002", "이미 사용중인 닉네임입니다.\n다른 닉네임을 이용해주세요"),
    CONFLICT_USER_EXCEPTION(CONFLICT, OFF, "CF003", "이미 해당 계정으로 회원가입하셨습니다.\n로그인 해주세요"),
    CONFLICT_DELETE_REQUEST_STORE_EXCEPTION(CONFLICT, OFF, "CF004", "이미 해당 가게에 삭제요청 하였습니다."),
    CONFLICT_VISIT_HISTORY_EXCEPTION(CONFLICT, OFF, "CF005", "오늘 이미 방문 인증한 가게입니다.\n다음에 다시 인증해주세요"),

    // 415 Unsupported Media Type
    UNSUPPORTED_MEDIA_TYPE_EXCEPTION(UNSUPPORTED_MEDIA_TYPE, OFF, "UM001", "해당하는 미디어 타입을 지원하지 않습니다."),

    // 500 Internal Server Exception
    INTERNAL_SERVER_EXCEPTION(INTERNAL_SERVER, ON, "IS001", "예상치 못한 에러가 발생하였습니다.\n잠시 후 다시 시도해주세요!"),
    INTERNAL_SERVER_UPDATE_STORE_OPTIMISTIC_LOCK_FAILED_EXCEPTION(INTERNAL_SERVER, ON, "IS002", "일시적으로 다른 사용자와 동시에 가게 수정 요청을 하였습니다ㅠㅠ\n잠시 후 다시 시도해주세요!"),

    // 502 Bad Gateway
    BAD_GATEWAY_EXCEPTION(BAD_GATEWAY, ON, "BG001", "일시적인 에러가 발생하였습니다.\n잠시 후 다시 시도해주세요!"),

    // 503 Service UnAvailable
    SERVICE_UNAVAILABLE_EXCEPTION(SERVICE_UNAVAILABLE, OFF, "SU001", "현재 점검 중입니다.\n잠시 후 다시 시도해주세요!"),
    ;

    private final HttpStatusCode statusCode;
    private final ErrorAlarmOptionType alarmOptions;
    private final String code;
    private final String message;

    public int getStatus() {
        return statusCode.getStatus();
    }

    public boolean isSetAlarm() {
        return this.alarmOptions.isSetAlarm();
    }

}
