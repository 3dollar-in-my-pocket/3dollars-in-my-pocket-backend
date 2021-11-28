package com.depromeet.threedollar.api.service.review.dto.request;

import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Deprecated
@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrieveMyReviewsV2Request {

    @Min(value = 1, message = "{common.size.min}")
    @Max(value = 100, message = "{common.size.max}")
    private int size;

    @Nullable
    private Long cursor;

    @Nullable
    private Long cachingTotalElements; // 총 리뷰 수를 매번 서버에서 조회하지 않고, 캐싱하기 위한 필드. (Optional)

    public static RetrieveMyReviewsV2Request testInstance(int size, Long cursor, Long cachingTotalElements) {
        return new RetrieveMyReviewsV2Request(size, cursor, cachingTotalElements);
    }

}
