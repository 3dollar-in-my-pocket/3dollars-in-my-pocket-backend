package com.depromeet.threedollar.api.service.review.dto.response.deprecated;

import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.domain.user.domain.review.Review;
import com.depromeet.threedollar.domain.user.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.user.domain.store.Store;
import com.depromeet.threedollar.domain.user.domain.user.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Deprecated
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewDetailV2Response extends AuditingTimeResponse {

    private Long reviewId;
    private int rating;
    private String contents;

    private Long storeId;
    private String storeName;
    private Boolean isDeletedStore;
    private final List<MenuCategoryType> categories = new ArrayList<>();

    private UserInfoResponse user;

    @Builder(access = AccessLevel.PRIVATE)
    private ReviewDetailV2Response(Long reviewId, int rating, String contents, Long storeId, String storeName, boolean isDeletedStore,
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

    public static ReviewDetailV2Response of(Review review, Store store, User user) {
        ReviewDetailV2Response response = ReviewDetailV2Response.builder()
            .reviewId(review.getId())
            .rating(review.getRating())
            .contents(review.getContents())
            .storeId(review.getStoreId())
            .storeName(store.getName())
            .isDeletedStore(store.isDeleted())
            .user(UserInfoResponse.of(user))
            .categories(store.getMenuCategoriesSortedByCounts())
            .build();
        response.setBaseTime(review.getCreatedAt(), review.getUpdatedAt());
        return response;
    }

}
