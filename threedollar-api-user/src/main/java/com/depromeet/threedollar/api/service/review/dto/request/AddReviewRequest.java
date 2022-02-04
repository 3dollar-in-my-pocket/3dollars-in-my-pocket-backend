package com.depromeet.threedollar.api.service.review.dto.request;

import com.depromeet.threedollar.domain.user.domain.review.Review;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddReviewRequest {

    @NotNull(message = "{store.storeId.notNull}")
    private Long storeId;

    @Length(max = 300, message = "{review.content.length}")
    @NotBlank(message = "{review.content.notBlank}")
    private String contents;

    @Min(value = 1, message = "{review.rating.min}")
    @Max(value = 5, message = "{review.rating.max}")
    private int rating;

    public static AddReviewRequest testInstance(Long storeId, String contents, int rating) {
        return new AddReviewRequest(storeId, contents, rating);
    }

    public Review toEntity(Long userId) {
        return Review.of(storeId, userId, contents, rating);
    }

}
