package com.depromeet.threedollar.common.exception.type;

import com.depromeet.threedollar.common.type.HttpStatusCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.depromeet.threedollar.common.exception.type.ErrorAlarmOptionType.*;
import static com.depromeet.threedollar.common.type.HttpStatusCode.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // 400 Bad Request
    VALIDATION_EXCEPTION(BAD_REQUEST, OFF, "BR001", "잘못된 요청입니다"),
    VALIDATION_APPLE_TOKEN_EXCEPTION(BAD_REQUEST, OFF, "BR002", "유효하지 않은 애플 토큰입니다"),
    VALIDATION_APPLE_TOKEN_EXPIRED_EXCEPTION(BAD_REQUEST, OFF, "BR003", "이미 만료된 애플 토큰입니다"),
    VALIDATION_REQUEST_MISSING_EXCEPTION(BAD_REQUEST, OFF, "BR005", "필수 요청 파라미터가 입력되지 않았습니다"),
    VALIDATION_WRONG_TYPE_EXCEPTION(BAD_REQUEST, OFF, "BR006", "잘못된 타입이 입력되었습니다"),
    VALIDATION_LATITUDE_EXCEPTION(BAD_REQUEST, OFF, "BR007", "현재 위치의 위도를 입력해주세요 (latitude)"),
    VALIDATION_LONGITUDE_EXCEPTION(BAD_REQUEST, OFF, "BR008", "현재 위치의 경도를 입력해주세요 (longitude)"),
    VALIDATION_MAP_LATITUDE_EXCEPTION(BAD_REQUEST, OFF, "BR009", "현재 지도상 위도를 입력해주세요 (mapLatitude)"),
    VALIDATION_MAP_LONGITUDE_EXCEPTION(BAD_REQUEST, OFF, "BR010", "현재 지도상 경도를 입력해주세요 (mapLongitude)"),


    // 401 UnAuthorized
    UNAUTHORIZED_EXCEPTION(UNAUTHORIZED, OFF, "UA001", "세션이 만료되었습니다. 다시 로그인 해주세요"),


    // 403 Forbidden
    FORBIDDEN_EXCEPTION(FORBIDDEN, OFF, "FB001", "허용하지 않는 요청입니다"),
    FORBIDDEN_RATING_EXCEPTION(BAD_REQUEST, OFF, "FB002", "허용되지 않은 평가 점수입니다\n(1 ~ 5 사이의 평가 점수를 입력해주세요)"), // TODO 403에러로 변경
    FORBIDDEN_LATITUDE_EXCEPTION(BAD_REQUEST, OFF, "FB003", "허용되지 않은 위도 범위를 입력하였습니다\n(33.1 ~ 38.61 사이의 위도를 입력해주세요)"), // TODO 차후 403에러로 변경
    FORBIDDEN_LONGITUDE_EXCEPTION(BAD_REQUEST, OFF, "FB004", "허용되지 않은 경도 범위를 입력하였습니다\n(124.60 ~ 131.87 사이의 경도를 입력해주세요)"), // TODO 차후 403에러로 변경
    FORBIDDEN_FILE_NAME_EXCEPTION(BAD_REQUEST, OFF, "FB005", "허용되지 않은 파일 이름입니다"), // TODO 차후 403에러로 변경
    FORBIDDEN_FILE_TYPE_EXCEPTION(BAD_REQUEST, OFF, "FB006", "허용되지 않은 파일 형식입니다"), // TODO 차후 403에러로 변경
    FORBIDDEN_UPLOAD_SIZE_EXCEPTION(BAD_REQUEST, ON, "FB007", "업로드 가능한 최대 파일 크기를 초과했습니다"), // TODO 차후 403에러로 변경
    FORBIDDEN_BOSS_STORE_OWNER_EXCEPTION(FORBIDDEN, OFF, "FB008", "해당 가게의 사장님만이 접근할 수 있습니다"),


    // 404 Not Found
    NOT_FOUND_EXCEPTION(NOT_FOUND, OFF, "NF001", "존재하지 않습니다"),
    NOT_FOUND_USER_EXCEPTION(NOT_FOUND, OFF, "NF002", "탈퇴하거나 존재하지 않는 유저입니다"),
    NOT_FOUND_STORE_EXCEPTION(NOT_FOUND, OFF, "NF003", "삭제되거나 존재하지 않는 가게입니다"),
    NOT_FOUND_REVIEW_EXCEPTION(NOT_FOUND, OFF, "NF004", "삭제되거나 존재하지 않는 리뷰입니다"),
    NOT_FOUND_STORE_IMAGE_EXCEPTION(NOT_FOUND, OFF, "NF005", "삭제되거나 존재하지 않는 가게 이미지입니다"),
    NOT_FOUND_FAQ_EXCEPTION(NOT_FOUND, OFF, "NF006", "삭제되거나 존재하지 않는 FAQ입니다"),
    NOT_FOUND_MEDAL_EXCEPTION(NOT_FOUND, OFF, "N007", "보유하고 있지 않은 메달입니다"),
    NOT_FOUND_CATEGORY_EXCEPTION(NOT_FOUND, OFF, "N009", "존재하지 않는 카테고리 입니다"),
    NOT_FOUND_BOSS_EXCEPTION(NOT_FOUND, OFF, "N010", "존재하지 않는 사장님 계정입니다"),
    NOT_FOUND_BOSS_OWNED_STORE_EXCEPTION(NOT_FOUND, OFF, "NF011", "사장님이 운영하는 가게가 존재하지 않습니다."),


    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED_EXCEPTION(METHOD_NOT_ALLOWED, OFF, "MN001","Not Allowed Method"),


    // 406 Not Acceptable
    NOT_ACCEPTABLE_EXCEPTION(NOT_ACCEPTABLE, OFF, "NA001","Not Acceptable"),


    // 409 Conflict
    CONFLICT_EXCEPTION(CONFLICT, OFF, "CF001","이미 존재합니다"),
    CONFLICT_NICKNAME_EXCEPTION(CONFLICT, OFF, "CF002","이미 사용중인 닉네임입니다.\n다른 닉네임을 이용해주세요"),
    CONFLICT_USER_EXCEPTION(CONFLICT, OFF, "CF003","이미 해당 계정으로 회원가입하셨습니다.\n로그인 해주세요"),
    CONFLICT_DELETE_REQUEST_STORE_EXCEPTION(CONFLICT, OFF, "CF004","이미 해당하는 가게에 삭제요청 하였습니다."),
    CONFLICT_VISIT_HISTORY_EXCEPTION(CONFLICT, OFF, "CF005","오늘 이미 방문 인증한 가게입니다.\n다음에 다시 인증해주세요"),
    CONFLICT_REGISTER_BOSS(CONFLICT, OFF, "CF006","이미 사장님 가입을 신청하셨습니다"),


    // 415 Unsupported Media Type
    UNSUPPORTED_MEDIA_TYPE_EXCEPTION(UNSUPPORTED_MEDIA_TYPE, OFF, "UM001","Unsupported Media Type"),


    // 500 Internal Server Exception
    INTERNAL_SERVER_EXCEPTION(INTERNAL_SERVER, ON, "IS001","예상치 못한 에러가 발생하였습니다.\n잠시 후 다시 시도해주세요!"),
    INTERNAL_SERVER_UPDATE_STORE_OPTIMISTIC_LOCK_FAILED_EXCEPTION(INTERNAL_SERVER, ON, "IS002","일시적으로 다른 사용자와 동시에 가게 수정 요청을 하였습니다ㅠㅠ\n잠시 후 다시 시도해주세요!"),


    // 502 Bad Gateway
    BAD_GATEWAY_EXCEPTION(BAD_GATEWAY, ON, "BG001","일시적인 에러가 발생하였습니다.\n잠시 후 다시 시도해주세요!"),


    // 503 Service UnAvailable
    SERVICE_UNAVAILABLE_EXCEPTION(SERVICE_UNAVAILABLE, OFF, "SU001","현재 점검 중입니다.\n잠시 후 다시 시도해주세요!"),
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
