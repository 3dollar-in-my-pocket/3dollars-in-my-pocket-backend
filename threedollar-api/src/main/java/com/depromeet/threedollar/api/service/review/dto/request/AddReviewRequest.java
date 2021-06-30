package com.depromeet.threedollar.api.service.review.dto.request;

import com.depromeet.threedollar.domain.domain.review.Review;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddReviewRequest {

    @NotNull(message = "{store.storeId.notnull}")
    private Long storeId;

    @NotBlank(message = "{review.content.notBlank}")
    private String content;

    @Min(value = 0, message = "{review.rating.min}")
    @Max(value = 5, message = "{review.rating.max}")
    private int rating;

    public static AddReviewRequest testInstance(Long storeId, String content, int rating) {
        return new AddReviewRequest(storeId, content, rating);
    }

    public Review toEntity(Long userId) {
        return Review.of(storeId, userId, content, rating);
    }

}
