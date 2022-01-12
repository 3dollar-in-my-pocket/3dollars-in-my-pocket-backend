package com.depromeet.threedollar.api.service.review.dto.request;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateReviewRequest {

    @NotBlank(message = "{review.content.notBlank}")
    private String contents;

    @Min(value = 1, message = "{review.rating.min}")
    @Max(value = 5, message = "{review.rating.max}")
    private int rating;

    public static UpdateReviewRequest testInstance(String contents, int rating) {
        return new UpdateReviewRequest(contents, rating);
    }

}
