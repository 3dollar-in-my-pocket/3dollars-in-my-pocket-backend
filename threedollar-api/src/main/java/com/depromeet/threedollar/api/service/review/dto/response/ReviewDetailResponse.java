package com.depromeet.threedollar.api.service.review.dto.response;

import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.store.Store;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewDetailResponse extends AuditingTimeResponse {

    private Long reviewId;
    private int rating;
    private String contents;
    private Long storeId;
    private String storeName;
    private Boolean isDeletedStore;
    private UserInfoResponse user;
    private final List<MenuCategoryType> categories = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private ReviewDetailResponse(Long reviewId, int rating, String contents, Long storeId, String storeName, boolean isDeletedStore,
                                 UserInfoResponse user, List<MenuCategoryType> categories) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.contents = contents;
        this.storeId = storeId;
        this.storeName = storeName;
        this.isDeletedStore = isDeletedStore;
        this.user = user;
        this.categories.addAll(categories);
    }

    public static ReviewDetailResponse of(ReviewWithWriterProjection review, Store store) {
        ReviewDetailResponse response = ReviewDetailResponse.builder()
            .reviewId(review.getReviewId())
            .rating(review.getRating())
            .contents(review.getContents())
            .storeId(review.getStoreId())
            .storeName(store.getName())
            .isDeletedStore(store.isDeleted())
            .user(UserInfoResponse.of(review.getUserId(), review.getUserName(), review.getUserSocialType()))
            .categories(store.getMenuCategoriesSortedByCounts())
            .build();
        response.setBaseTime(review.getCreatedAt(), review.getUpdatedAt());
        return response;
    }

}
