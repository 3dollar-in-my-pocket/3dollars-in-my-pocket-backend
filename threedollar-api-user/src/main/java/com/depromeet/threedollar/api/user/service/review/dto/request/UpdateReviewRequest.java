package com.depromeet.threedollar.api.user.service.review.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateReviewRequest {

    @Length(max = 300, message = "{review.content.length}")
    @NotBlank(message = "{review.content.notBlank}")
    private String contents;

    @Min(value = 1, message = "{review.rating.min}")
    @Max(value = 5, message = "{review.rating.max}")
    private int rating;

    public static UpdateReviewRequest testInstance(String contents, int rating) {
        return new UpdateReviewRequest(contents, rating);
    }

}
