package com.depromeet.threedollar.common.exception.type;

import static com.depromeet.threedollar.common.exception.type.ErrorAlarmOptionType.OFF;
import static com.depromeet.threedollar.common.exception.type.ErrorAlarmOptionType.ON;
import static com.depromeet.threedollar.common.type.HttpStatusCode.BAD_REQUEST;

import com.depromeet.threedollar.common.type.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 400 Bad Request
    INVALID(BAD_REQUEST, OFF, "BR000", "잘못된 요청입니다"),
    INVALID_TYPE(BAD_REQUEST, OFF, "BR001", "잘못된 타입이 입력되었습니다"),
    INVALID_ENCODING_ID(BAD_REQUEST, OFF, "BR002", "잘못된 id가 입력되었습니다"),

    INVALID_MISSING_PARAMETER(BAD_REQUEST, OFF, "BR100", "필수 파라미터가 입력되지 않았습니다"),
    INVALID_MISSING_LATITUDE(BAD_REQUEST, OFF, "BR101", "디바이스의 위도를 입력해주세요 (latitude)"),
    INVALID_MISSING_LONGITUDE(BAD_REQUEST, OFF, "BR102", "디바이스의 경도를 입력해주세요 (longitude)"),
    INVALID_MISSING_MAP_LATITUDE(BAD_REQUEST, OFF, "BR103", "지도 위도를 입력해주세요 (mapLatitude)"),
    INVALID_MISSING_MAP_LONGITUDE(BAD_REQUEST, OFF, "BR104", "지도 경도를 입력해주세요 (mapLongitude)"),
    INVALID_MISSING_AUTH_TOKEN(BAD_REQUEST, OFF, "BR105", "인증 토큰을 입력해주세요"),
    INVALID_EMPTY_FILES(BAD_REQUEST, OFF, "BR106", "파일을 업로드해주세요"),

    INVALID_AUTH_TOKEN(BAD_REQUEST, OFF, "BR200", "만료되거나 유효하지 않은 인증 토큰입니다"),
    INVALID_CONTACTS_NUMBER_FORMAT(BAD_REQUEST, OFF, "BR201", "잘못된 연락처 번호입니다."),
    INVALID_BUSINESS_NUMBER_FORMAT(BAD_REQUEST, OFF, "BR202", "잘못된 사업자 번호입니다"),
    INVALID_RATING_RANGE(BAD_REQUEST, OFF, "BR203", "잘못된 평가 점수입니다\n(1 ~ 5 사이의 평가 점수를 입력해주세요)"),
    INVALID_LATITUDE_RANGE(BAD_REQUEST, OFF, "BR204", "잘못된 위도를 입력하였습니다\n(33.1 ~ 38.61 사이의 위도를 입력해주세요)"),
    INVALID_LONGITUDE_RANGE(BAD_REQUEST, OFF, "BR205", "잘못된 경도를 입력하였습니다\n(124.60 ~ 131.87 사이의 경도를 입력해주세요)"),
    INVALID_EMPTY_UPLOAD_FILE_NAME(BAD_REQUEST, OFF, "BR206", "잘못된 파일입니다\n파일의 이름이 없습니다."),
    INVALID_UPLOAD_FILE_TYPE(BAD_REQUEST, OFF, "BR207", "잘못된 파일 확장자입니다"),
    INVALID_UPLOAD_FILE_SIZE(BAD_REQUEST, ON, "BR208", "업로드 가능한 파일 크기를 초과했습니다"),
    INVALID_DATE_TIME_INTERVAL(BAD_REQUEST, OFF, "BR209", "시작 날짜가 종료 날짜보다 이후일 수 없습니다"),
    INVALID_EXCESS_MAX_BETWEEN_DAY_DIFFERENCE(BAD_REQUEST, OFF, "BR210", "시작 날짜와 종료날짜 간 차이는 최대 15일 이내로 조회해주세요"),
    INVALID_EMAIL_FORMAT(BAD_REQUEST, OFF, "BR211", "잘못된 이메일 포맷입니다"),


    // 401 UnAuthorized
    UNAUTHORIZED(HttpStatusCode.UNAUTHORIZED, OFF, "UA000", "세션이 만료되었습니다. 다시 로그인 해주세요"),


    // 403 Forbidden
    FORBIDDEN(HttpStatusCode.FORBIDDEN, OFF, "FB000", "허용하지 않는 요청입니다"),
    FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT(HttpStatusCode.FORBIDDEN, OFF, "FB001", "가입 신청이 승인 대기 중입니다\n가입 승인 절차 이후 이용하실 수 있습니다."),
    FORBIDDEN_UPLOAD_FILE_IN_MODULE(HttpStatusCode.FORBIDDEN, OFF, "FB002", "해당 서비스에서 업로드할 수 없는 파일 타입 입니다"),
    FORBIDDEN_NOT_SUPPORTED_FAQ_CATEGORY(HttpStatusCode.FORBIDDEN, OFF, "FB003", "해당 서비스에서 지원하지 않는 FAQ 카테고리 입니다"),
    FORBIDDEN_NOT_SUPPORTED_ADVERTISEMENT_POSITION(HttpStatusCode.FORBIDDEN, OFF, "FB004", "해당 서비스에서 지원하지 않는 광고 위치 입니다"),
    FORBIDDEN_NOT_OPEN_STORE(HttpStatusCode.FORBIDDEN, OFF, "FB005", "현재 영업중인 가게가 아닙니다"),


    // 404 Not Found
    NOT_FOUND(HttpStatusCode.NOT_FOUND, OFF, "NF000", "존재하지 않습니다"),
    NOT_FOUND_USER(HttpStatusCode.NOT_FOUND, OFF, "NF001", "탈퇴하거나 존재하지 않는 유저입니다"),
    NOT_FOUND_STORE(HttpStatusCode.NOT_FOUND, OFF, "NF002", "삭제되거나 존재하지 않는 가게입니다"),
    NOT_FOUND_REVIEW(HttpStatusCode.NOT_FOUND, OFF, "NF003", "삭제되거나 존재하지 않는 리뷰입니다"),
    NOT_FOUND_STORE_IMAGE(HttpStatusCode.NOT_FOUND, OFF, "NF004", "삭제되거나 존재하지 않는 가게 이미지입니다"),
    NOT_FOUND_FAQ(HttpStatusCode.NOT_FOUND, OFF, "NF005", "삭제되거나 존재하지 않는 FAQ입니다"),
    NOT_FOUND_MEDAL(HttpStatusCode.NOT_FOUND, OFF, "N006", "존재하지 않은 메달입니다"),
    NOT_FOUND_USER_MEDAL(HttpStatusCode.NOT_FOUND, OFF, "N007", "보유하지 않은 메달입니다"),
    NOT_FOUND_CATEGORY(HttpStatusCode.NOT_FOUND, OFF, "N008", "존재하지 않는 카테고리 입니다"),
    NOT_FOUND_BOSS_ACCOUNT(HttpStatusCode.NOT_FOUND, OFF, "N009", "존재하지 않는 사장님 계정입니다"),
    NOT_FOUND_SIGNUP_REGISTRATION(HttpStatusCode.NOT_FOUND, OFF, "NF010", "해당하는 가입 신청은 존재하지 않습니다"),
    NOT_FOUND_ADMIN(HttpStatusCode.NOT_FOUND, OFF, "NF011", "해당하는 관리자는 존재하지 않습니다"),
    NOT_FOUND_ADVERTISEMENT(HttpStatusCode.NOT_FOUND, OFF, "NF012", "해당하는 광고는 존재하지 않습니다"),


    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(HttpStatusCode.METHOD_NOT_ALLOWED, OFF, "MN000", "허용되지 않은 HTTP 메소드입니다"),


    // 406 Not Acceptable
    NOT_ACCEPTABLE(HttpStatusCode.NOT_ACCEPTABLE, OFF, "NA000", "허용되지 않은 Content-Type 입니다"),


    // 409 Conflict
    CONFLICT(HttpStatusCode.CONFLICT, OFF, "CF000", "이미 존재합니다"),
    CONFLICT_NICKNAME(HttpStatusCode.CONFLICT, OFF, "CF001", "이미 사용중인 닉네임입니다.\n다른 닉네임을 이용해주세요"),
    CONFLICT_USER(HttpStatusCode.CONFLICT, OFF, "CF002", "이미 회원가입하셨습니다.\n해당 계정으로 로그인 해주세요"),
    CONFLICT_DELETE_REQUEST_STORE(HttpStatusCode.CONFLICT, OFF, "CF003", "이미 해당 가게를 삭제 요청 하였습니다."),
    CONFLICT_VISIT_HISTORY(HttpStatusCode.CONFLICT, OFF, "CF004", "오늘 이미 방문 인증한 가게입니다.\n내일 다시 방문 인증해주세요 :)"),
    CONFLICT_BOSS_STORE_FEEDBACK(HttpStatusCode.CONFLICT, OFF, "CF005", "오늘 이미 피드백을 추가한 가게입니다.\n내일 다시 인증해주세요 :)"),
    CONFLICT_BOSS_ACCOUNT(HttpStatusCode.CONFLICT, OFF, "CF006", "이미 가입 완료한 사장님입니다"),
    CONFLICT_EMAIL(HttpStatusCode.CONFLICT, OFF, "CF007", "이미 존재하는 이메일 입니다"),


    // 415 Unsupported Media Type
    UNSUPPORTED_MEDIA_TYPE(HttpStatusCode.UNSUPPORTED_MEDIA_TYPE, OFF, "UM000", "지원 하지 않는 MediaType 입니다/"),

    // 429 Too Many Requests
    TOO_MANY_REQUESTS(HttpStatusCode.TOO_MANY_REQUESTS, ON, "TM000", "일시적으로 많은 요청이 들어왔습니다\n잠시후 다시 이용해주세요"),


    // 500 Internal Server Exception
    INTERNAL_SERVER(HttpStatusCode.INTERNAL_SERVER, ON, "IS000", "예상치 못한 에러가 발생하였습니다ㅠ.ㅠ\n잠시 후 다시 시도해주세요!"),
    INTERNAL_SERVER_UPDATE_STORE_OPTIMISTIC_LOCK_FAILED(HttpStatusCode.INTERNAL_SERVER, ON, "IS002", "일시적으로 다른 사용자와 동시에 가게 수정 요청을 하였습니다\n잠시 후 다시 시도해주세요!"),


    // 502 Bad Gateway
    BAD_GATEWAY(HttpStatusCode.BAD_GATEWAY, ON, "BG000", "일시적인 에러가 발생하였습니다ㅠ.ㅠ\n잠시 후 다시 시도해주세요!"),


    // 503 Service UnAvailable
    SERVICE_UNAVAILABLE(HttpStatusCode.SERVICE_UNAVAILABLE, OFF, "SU000", "해당 기능은 현재 사용할 수 없습니다"),
    ;

    private final HttpStatusCode statusCode;
    private final ErrorAlarmOptionType alarmOptions;
    private final String code;
    private final String message;

    ErrorCode(HttpStatusCode statusCode, ErrorAlarmOptionType alarmOptions, String code, String message) {
        this.statusCode = statusCode;
        this.alarmOptions = alarmOptions;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return statusCode.getStatus();
    }

    public boolean isSetAlarm() {
        return this.alarmOptions.isSetAlarm();
    }

}
