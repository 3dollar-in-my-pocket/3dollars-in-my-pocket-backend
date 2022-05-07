package com.depromeet.threedollar.api.user.service.user.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CheckAvailableNameRequestTest {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @ValueSource(strings = {"디프만", "붕어빵", "가슴속-삼천원", "강승호"})
    @ParameterizedTest
    void 사용가능한_이름이면_유효성_검사를_통과한다(String name) {
        // given
        CheckAvailableNameRequest request = CheckAvailableNameRequest.testBuilder()
            .name(name)
            .build();

        // when
        Set<ConstraintViolation<CheckAvailableNameRequest>> constraintViolations = validator.validate(request);

        // then
        assertThat(constraintViolations).isEmpty();
    }

    @ValueSource(strings = {"-a-", "a--", "디", "디#프만", "디+프만"})
    @ParameterizedTest
    void 사용가능하지_않은_이름이면_유효성_검사에서_실패한다(String name) {
        // given
        CheckAvailableNameRequest request = CheckAvailableNameRequest.testBuilder()
            .name(name)
            .build();

        // when
        Set<ConstraintViolation<CheckAvailableNameRequest>> constraintViolations = validator.validate(request);

        // then
        assertThat(constraintViolations).isNotEmpty()
            .hasSize(1);
    }

    @NullAndEmptySource
    @ParameterizedTest
    void 닉네임이_NULL_이거나_빈문자열이면_유효성_검사에서_실패한다(String name) {
        // given
        CheckAvailableNameRequest request = CheckAvailableNameRequest.testBuilder()
            .name(name)
            .build();

        // when
        Set<ConstraintViolation<CheckAvailableNameRequest>> constraintViolations = validator.validate(request);

        // then
        assertThat(constraintViolations).isNotEmpty()
            .hasSize(1);
    }

}
