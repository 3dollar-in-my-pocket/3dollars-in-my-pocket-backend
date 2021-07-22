package com.depromeet.threedollar.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

    // Common
    VALIDATION_EXCEPTION("C001", "잘못된 요청입니다"),
    UNAUTHORIZED_EXCEPTION("C002", "토큰이 만료되었습니다. 다시 로그인 해주세요"),
    FORBIDDEN_EXCEPTION("C003", "허용하지 않는 접근입니다."),
    NOT_FOUND_EXCEPTION("C004", "존재하지 않습니다"),
    CONFLICT_EXCEPTION("C005", "이미 존재합니다"),
    INTERNAL_SERVER_EXCEPTION("C006", "서버 내부에서 에러가 발생하였습니다"),
    METHOD_NOT_ALLOWED_EXCEPTION("C007", "지원하지 않는 메소드 입니다"),
    BAD_GATEWAY_EXCEPTION("C008", "외부 연동 중 에러가 발생하였습니다"),
    SERVICE_UNAVAILABLE_EXCEPTION("C009", "서비스를 이용하실 수 없습니다"),

    // Not Found Exception
    NOT_FOUND_USER_EXCEPTION("N001", "존재하지 않는 유저입니다"),
    NOT_FOUND_STORE_EXCEPTION("N002", "존재하지 않는 가게입니다"),
    NOT_FOUND_REVIEW_EXCEPTION("N003", "존재하지 않는 리뷰입니다"),
    NOT_FOUND_STORE_IMAGE_EXCEPTION("N003", "존재하지 않는 가게 이미지입니다"),

    // Validation Exception
    VALIDATION_RATING_EXCEPTION("V001", "허용되지 않은 평가 점수입니다. (0 ~ 5)"),
    VALIDATION_LATITUDE_EXCEPTION("V002", "허용되지 않은 위도 범위를 입력하였습니다. (33 ~ 43)"),
    VALIDATION_LONGITUDE_EXCEPTION("V003", "허용되지 않은 경도 범위를 입력하였습니다. (124 ~ 132)"),
    VALIDATION_FILE_TYPE_EXCEPTION("V004", "허용되지 않은 파일 형식입니다"),
    ;

    private final String code;
    private final String message;

}
