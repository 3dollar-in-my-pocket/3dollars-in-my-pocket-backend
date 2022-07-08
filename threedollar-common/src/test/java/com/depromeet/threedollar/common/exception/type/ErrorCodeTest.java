package com.depromeet.threedollar.common.exception.type;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

class ErrorCodeTest {

    @DisplayName("에러 코드에 대한 중복 방지를 위한 테스트")
    @Test
    void 외부로_보여지는_에러코드가_유니크한_값을_가져야한다() {
        // given
        List<String> errorCodes = new ArrayList<>();

        // when & then
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCodes.contains(errorCode.getCode())) {
                fail(String.format("중복되는 에러 (%s) 코드(%s)가 존재합니다", errorCode, errorCode.getCode()));
            }
            errorCodes.add(errorCode.getCode());
        }
    }

}
