package com.depromeet.threedollar.common.exception.type;

import com.depromeet.threedollar.common.type.HttpStatusCode;
import lombok.Getter;

import static com.depromeet.threedollar.common.exception.type.ErrorAlarmOptionType.OFF;
import static com.depromeet.threedollar.common.exception.type.ErrorAlarmOptionType.ON;
import static com.depromeet.threedollar.common.type.HttpStatusCode.BAD_GATEWAY;
import static com.depromeet.threedollar.common.type.HttpStatusCode.BAD_REQUEST;
import static com.depromeet.threedollar.common.type.HttpStatusCode.CONFLICT;
import static com.depromeet.threedollar.common.type.HttpStatusCode.FORBIDDEN;
import static com.depromeet.threedollar.common.type.HttpStatusCode.INTERNAL_SERVER;
import static com.depromeet.threedollar.common.type.HttpStatusCode.METHOD_NOT_ALLOWED;
import static com.depromeet.threedollar.common.type.HttpStatusCode.NOT_ACCEPTABLE;
import static com.depromeet.threedollar.common.type.HttpStatusCode.NOT_FOUND;
import static com.depromeet.threedollar.common.type.HttpStatusCode.NOT_IMPLEMENTED;
import static com.depromeet.threedollar.common.type.HttpStatusCode.SERVICE_UNAVAILABLE;
import static com.depromeet.threedollar.common.type.HttpStatusCode.TOO_MANY_REQUESTS;
import static com.depromeet.threedollar.common.type.HttpStatusCode.UNAUTHORIZED;
import static com.depromeet.threedollar.common.type.HttpStatusCode.UNSUPPORTED_MEDIA_TYPE;

@Getter
public enum ErrorCode {

    /**
     * 400 Bad Request (잘못된 요청)
     */
    E400_INVALID(BAD_REQUEST, OFF, "BR000", "잘못된 요청입니다"),
    E400_INVALID_ENCODING_ID(BAD_REQUEST, OFF, "BR001", "잘못된 ID가 입력되었습니다"),
    E400_INVALID_AUTH_TOKEN(BAD_REQUEST, OFF, "BR002", "만료되거나 유효하지 않은 소셜 인증 토큰입니다"),
    E400_INVALID_CONTACTS_NUMBER_FORMAT(BAD_REQUEST, OFF, "BR003", "잘못된 연락처 번호입니다."),
    E400_INVALID_BUSINESS_NUMBER_FORMAT(BAD_REQUEST, OFF, "BR004", "잘못된 사업자 번호입니다"),
    E400_INVALID_RATING_RANGE(BAD_REQUEST, OFF, "BR005", "잘못된 평가 점수입니다\n(1 ~ 5 사이의 평가 점수를 입력해주세요)"),
    E400_INVALID_LATITUDE_RANGE(BAD_REQUEST, OFF, "BR006", "잘못된 위도를 입력하였습니다\n(33.1 ~ 38.61 사이의 위도를 입력해주세요)"),
    E400_INVALID_LONGITUDE_RANGE(BAD_REQUEST, OFF, "BR007", "잘못된 경도를 입력하였습니다\n(124.60 ~ 131.87 사이의 경도를 입력해주세요)"),
    E400_INVALID_UPLOAD_FILE_EXTENSION(BAD_REQUEST, OFF, "BR008", "잘못된 파일 확장자입니다"),
    E400_INVALID_FILE_SIZE_TOO_LARGE(BAD_REQUEST, ON, "BR009", "업로드 가능한 파일 크기를 초과했습니다"),
    E400_INVALID_DATE_TIME_INTERVAL(BAD_REQUEST, OFF, "BR010", "시작 날짜가 종료 날짜보다 이후일 수 없습니다"),
    E400_INVALID_EXCESS_BETWEEN_START_DATE_AND_END_DATE(BAD_REQUEST, OFF, "BR011", "시작 날짜와 종료날짜 간 차이는 최대 15일 이내로 조회해주세요"),
    E400_INVALID_EMAIL_FORMAT(BAD_REQUEST, OFF, "BR012", "잘못된 이메일 포맷입니다"),

    E400_MISSING_PARAMETER(BAD_REQUEST, OFF, "BR100", "필수 파라미터가 입력되지 않았습니다"),
    E400_MISSING_LATITUDE_PARAMETER(BAD_REQUEST, OFF, "BR101", "디바이스의 위도를 입력해주세요 (latitude)"),
    E400_MISSING_LONGITUDE_PARAMETER(BAD_REQUEST, OFF, "BR102", "디바이스의 경도를 입력해주세요 (longitude)"),
    E400_MISSING_MAP_LATITUDE_PARAMETER(BAD_REQUEST, OFF, "BR103", "지도 위도를 입력해주세요 (mapLatitude)"),
    E400_MISSING_MAP_LONGITUDE_PARAMETER(BAD_REQUEST, OFF, "BR104", "지도 경도를 입력해주세요 (mapLongitude)"),
    E400_MISSING_AUTH_TOKEN_PARAMETER(BAD_REQUEST, OFF, "BR105", "인증 토큰을 입력해주세요"),
    E400_MISSING_FILE(BAD_REQUEST, OFF, "BR106", "파일을 업로드해주세요"),
    E400_MISSING_UPLOAD_FILE_NAME(BAD_REQUEST, OFF, "BR107", "잘못된 파일입니다\n파일의 이름이 없습니다."),


    /**
     * 401 UnAuthorized (인증 실패)
     */
    E401_UNAUTHORIZED(UNAUTHORIZED, OFF, "UA000", "세션이 만료되었습니다. 다시 로그인 해주세요"),


    /**
     * 403 Forbidden (권한 등의 이유로 허용하지 않는 요청)
     */
    E403_FORBIDDEN(FORBIDDEN, OFF, "FB000", "허용하지 않는 요청입니다"),
    E403_FORBIDDEN_WAITING_STATUS_TO_APPROVE_BOSS_ACCOUNT(FORBIDDEN, OFF, "FB001", "가입 신청이 승인 대기 중입니다\n가입 승인 절차 이후 이용하실 수 있습니다."),
    E403_FORBIDDEN_NOT_OPENING_STORE(FORBIDDEN, OFF, "FB002", "현재 영업중인 가게가 아닙니다"),


    /**
     * 404 Not Found (존재하지 않는 리소스)
     */
    E404_NOT_EXISTS(NOT_FOUND, OFF, "NF000", "존재하지 않습니다"),
    E404_NOT_EXISTS_USER(NOT_FOUND, OFF, "NF001", "탈퇴하거나 존재하지 않는 유저입니다"),
    E404_NOT_EXISTS_STORE(NOT_FOUND, OFF, "NF002", "삭제되거나 존재하지 않는 가게입니다"),
    E404_NOT_EXISTS_REVIEW(NOT_FOUND, OFF, "NF003", "삭제되거나 존재하지 않는 리뷰입니다"),
    E404_NOT_EXISTS_STORE_IMAGE(NOT_FOUND, OFF, "NF004", "삭제되거나 존재하지 않는 가게 이미지입니다"),
    E404_NOT_EXISTS_FAQ(NOT_FOUND, OFF, "NF005", "삭제되거나 존재하지 않는 FAQ입니다"),
    E404_NOT_EXISTS_MEDAL(NOT_FOUND, OFF, "N006", "존재하지 않은 메달입니다"),
    E404_NOT_EXISTS_USER_MEDAL(NOT_FOUND, OFF, "N007", "보유하지 않은 메달입니다"),
    E404_NOT_EXISTS_CATEGORY(NOT_FOUND, OFF, "N008", "존재하지 않는 카테고리 입니다"),
    E404_NOT_EXISTS_BOSS_ACCOUNT(NOT_FOUND, OFF, "N009", "존재하지 않는 사장님 계정입니다"),
    E404_NOT_EXISTS_SIGNUP_REGISTRATION(NOT_FOUND, OFF, "NF010", "해당하는 가입 신청은 존재하지 않습니다"),
    E404_NOT_EXISTS_ADMIN(NOT_FOUND, OFF, "NF011", "해당하는 관리자는 존재하지 않습니다"),
    E404_NOT_EXISTS_ADVERTISEMENT(NOT_FOUND, OFF, "NF012", "해당하는 광고는 존재하지 않습니다"),


    /**
     * 405 Method Not Allowed
     */
    E405_METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED, OFF, "MN000", "허용되지 않은 HTTP 메소드입니다"),


    /**
     * 406 Not Acceptable
     */
    E406_NOT_ACCEPTABLE(NOT_ACCEPTABLE, OFF, "NA000", "허용되지 않은 Content-Type 입니다"),


    /**
     * 409 Conflict (중복되는 리소스)
     */
    E409_DUPLICATE(CONFLICT, OFF, "CF000", "이미 존재합니다"),
    E409_DUPLICATE_NICKNAME(CONFLICT, OFF, "CF001", "이미 사용중인 닉네임입니다.\n다른 닉네임을 이용해주세요"),
    E409_DUPLICATE_USER(CONFLICT, OFF, "CF002", "이미 회원가입하셨습니다.\n해당 계정으로 로그인 해주세요"),
    E409_DUPLICATE_DELETE_REQUEST_STORE(CONFLICT, OFF, "CF003", "이미 해당 가게를 삭제 요청 하였습니다."),
    E409_DUPLICATE_VISIT_HISTORY(CONFLICT, OFF, "CF004", "오늘 이미 방문 인증한 가게입니다.\n내일 다시 방문 인증해주세요 :)"),
    E409_DUPLICATE_BOSS_STORE_FEEDBACK(CONFLICT, OFF, "CF005", "오늘 이미 피드백을 추가한 가게입니다.\n내일 다시 인증해주세요 :)"),
    E409_DUPLICATE_BOSS_ACCOUNT(CONFLICT, OFF, "CF006", "이미 가입 완료한 사장님입니다"),
    E409_DUPLICATE_EMAIL(CONFLICT, OFF, "CF007", "이미 존재하는 이메일 입니다"),


    /**
     * 415 Unsupported Media Type
     */
    E415_UNSUPPORTED_MEDIA_TYPE(UNSUPPORTED_MEDIA_TYPE, OFF, "UM000", "지원 하지 않는 MediaType 입니다/"),


    /**
     * 429 Too Many Requests (RateLimit)
     */
    E429_TOO_MANY_REQUESTS(TOO_MANY_REQUESTS, ON, "TM000", "일시적으로 많은 요청이 들어왔습니다\n잠시후 다시 이용해주세요"),


    /**
     * 500 Internal Server Exception (서버 내부 에러)
     */
    E500_INTERNAL_SERVER(INTERNAL_SERVER, ON, "IS000", "예상치 못한 에러가 발생하였습니다ㅠ.ㅠ\n잠시 후 다시 시도해주세요!"),
    E500_INTERNAL_SERVER_UPDATE_STORE_CONCURRENCY(INTERNAL_SERVER, ON, "IS002", "일시적으로 다른 사용자와 동시에 가게 수정 요청을 하였습니다\n잠시 후 다시 시도해주세요!"),


    /**
     * 501 Not Implemented (현재 지원하지 않는 요청)
     */
    E501_NOT_SUPPORTED(NOT_IMPLEMENTED, OFF, "II000", "지원하지 않는 요청입니다"),
    E501_NOT_SUPPORTED_UPLOAD_FILE(NOT_IMPLEMENTED, OFF, "II001", "해당 서비스에서 업로드할 수 없는 파일 타입 입니다"),
    E501_NOT_SUPPORTED_FAQ_CATEGORY(NOT_IMPLEMENTED, OFF, "II002", "해당 서비스에서 지원하지 않는 FAQ 카테고리 입니다"),
    E501_NOT_SUPPORTED_ADVERTISEMENT_POSITION(NOT_IMPLEMENTED, OFF, "II003", "해당 서비스에서 지원하지 않는 광고 위치 입니다"),
    E501_NOT_SUPPORTED_PUSH_PLATFORM(NOT_IMPLEMENTED, OFF, "II004", "해당 디바이스에서 지원하지 않는 푸시 플랫폼 입니다"),


    /**
     * 502 Bad Gateway (외부 시스템의 Bad Gateway)
     */
    E502_BAD_GATEWAY(BAD_GATEWAY, ON, "BG000", "일시적인 에러가 발생하였습니다ㅠ.ㅠ\n잠시 후 다시 시도해주세요!"),


    /**
     * 503 Service UnAvailable
     */
    E503_SERVICE_UNAVAILABLE(SERVICE_UNAVAILABLE, OFF, "SU000", "해당 기능은 현재 사용할 수 없습니다"),
    E503_SERVICE_UNAVAILABLE_UNDER_INSPECTION(SERVICE_UNAVAILABLE, OFF, "SU001", "현재 점검중인 기능입니다\n불편을 드려 죄송합니다!"),
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
