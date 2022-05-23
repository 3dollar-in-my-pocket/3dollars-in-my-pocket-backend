package com.depromeet.threedollar.api.vendor.service.review.dto.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateReviewRequest {

    @Size(max = 300, message = "{review.content.size}")
    @NotBlank(message = "{review.content.notBlank}")
    private String contents;

    @Min(value = 1, message = "{review.rating.min}")
    @Max(value = 5, message = "{review.rating.max}")
    private int rating;

    @Builder(builderMethodName = "testBuilder")
    private UpdateReviewRequest(String contents, int rating) {
        this.contents = contents;
        this.rating = rating;
    }

}
