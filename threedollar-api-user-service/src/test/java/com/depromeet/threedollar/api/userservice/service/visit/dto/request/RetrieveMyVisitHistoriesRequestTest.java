package com.depromeet.threedollar.api.userservice.service.visit.dto.request;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RetrieveMyVisitHistoriesRequestTest {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @ValueSource(ints = {0, 101})
    @ParameterizedTest
    void 허용된_범위_밖의_SIZE가_입력되면_ValidationException(int size) {
        // given
        RetrieveMyVisitHistoriesRequest request = RetrieveMyVisitHistoriesRequest.testBuilder()
            .size(size)
            .cursor(null)
            .build();

        // when
        Set<ConstraintViolation<RetrieveMyVisitHistoriesRequest>> constraintViolations = validator.validate(request);

        // then
        assertThat(constraintViolations).hasSize(1);
    }

    @ValueSource(ints = {1, 100})
    @ParameterizedTest
    void 허용된_범위_내의_SIZE가_입력되면_통과한다(int size) {
        // given
        RetrieveMyVisitHistoriesRequest request = RetrieveMyVisitHistoriesRequest.testBuilder()
            .size(size)
            .cursor(null)
            .build();

        // when
        Set<ConstraintViolation<RetrieveMyVisitHistoriesRequest>> constraintViolations = validator.validate(request);

        // then
        assertThat(constraintViolations).isEmpty();
    }

}
