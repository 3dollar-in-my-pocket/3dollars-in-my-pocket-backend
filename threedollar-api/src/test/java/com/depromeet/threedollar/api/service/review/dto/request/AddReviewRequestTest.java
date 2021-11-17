package com.depromeet.threedollar.api.service.review.dto.request;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AddReviewRequestTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @ValueSource(ints = {0, 6})
    @ParameterizedTest
    void 리뷰_평가가_1점_미만_혹은_5점_초과이면_유효성_검사가_실패한다(int rating) {
        // given
        Long storeId = 100L;
        String contents = "댓글";

        AddReviewRequest request = AddReviewRequest.testInstance(storeId, contents, rating);

        // when
        Set<ConstraintViolation<AddReviewRequest>> constraintViolations = validator.validate(request);

        // then
        assertThat(constraintViolations).isNotEmpty();
    }

    @ValueSource(ints = {1, 2, 3, 4, 5})
    @ParameterizedTest
    void 리뷰_평가가_1점_이상_5점_이하이면_유효성_검사를_통과한다(int rating) {
        // given
        Long storeId = 100L;
        String contents = "댓글";

        AddReviewRequest request = AddReviewRequest.testInstance(storeId, contents, rating);

        // when
        Set<ConstraintViolation<AddReviewRequest>> constraintViolations = validator.validate(request);

        // then
        assertThat(constraintViolations).isEmpty();
    }

}
