package com.depromeet.threedollar.api.service.review.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateReviewRequest {

	private String content;

	private int rating;

	public static UpdateReviewRequest testInstance(String content, int rating) {
		return new UpdateReviewRequest(content, rating);
	}

}
