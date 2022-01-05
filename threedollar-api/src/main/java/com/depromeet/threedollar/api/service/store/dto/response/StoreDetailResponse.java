package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.api.service.review.dto.response.ReviewWithUserResponse;
import com.depromeet.threedollar.api.service.storeimage.dto.response.StoreImageResponse;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryCountsResponse;
import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryWithUserResponse;
import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.common.utils.LocationDistanceUtils;
import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.store.*;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.collection.user.UserDictionary;
import com.depromeet.threedollar.domain.collection.visit.VisitHistoryCounter;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreDetailResponse extends AuditingTimeResponse {

    // 가게
    private Long storeId;
    private double latitude;
    private double longitude;
    private String storeName;
    private StoreType storeType;
    private double rating;
    private int distance;
    private final List<MenuCategoryType> categories = new ArrayList<>();
    private final Set<DayOfTheWeek> appearanceDays = new HashSet<>();
    private final Set<PaymentMethodType> paymentMethods = new HashSet<>();
    private final List<MenuResponse> menus = new ArrayList<>();

    // 작성자
    private UserInfoResponse user;

    // 가게 이미지
    private final List<StoreImageResponse> images = new ArrayList<>();

    // 리뷰
    private final List<ReviewWithUserResponse> reviews = new ArrayList<>();

    // 방문 인증
    private VisitHistoryCountsResponse visitHistory;
    private final List<VisitHistoryWithUserResponse> visitHistories = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    private StoreDetailResponse(Long storeId, double latitude, double longitude, String storeName, StoreType storeType,
                                double rating, int distance, UserInfoResponse user, VisitHistoryCountsResponse visitHistory) {
        this.storeId = storeId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeName = storeName;
        this.storeType = storeType;
        this.rating = rating;
        this.distance = distance;
        this.user = user;
        this.visitHistory = visitHistory;
    }

    public static StoreDetailResponse of(Store store, double latitude, double longitude, List<StoreImage> storeImages, UserDictionary userDictionary,
                                         List<Review> reviews, VisitHistoryCounter visitHistoriesCollection, List<VisitHistoryWithUserProjection> visitHistories) {
        StoreDetailResponse response = StoreDetailResponse.builder()
            .storeId(store.getId())
            .latitude(store.getLatitude())
            .longitude(store.getLongitude())
            .storeName(store.getName())
            .storeType(store.getType())
            .rating(store.getRating())
            .distance(LocationDistanceUtils.getDistance(store.getLatitude(), store.getLongitude(), latitude, longitude))
            .user(UserInfoResponse.of(userDictionary.getUser(store.getUserId())))
            .visitHistory(VisitHistoryCountsResponse.of(visitHistoriesCollection.getStoreExistsVisitsCount(store.getId()), visitHistoriesCollection.getStoreNotExistsVisitsCount(store.getId())))
            .build();
        response.categories.addAll(store.getMenuCategoriesSortedByCounts());
        response.appearanceDays.addAll(store.getAppearanceDayTypes());
        response.paymentMethods.addAll(store.getPaymentMethodTypes());
        response.images.addAll(toImageResponse(storeImages));
        response.menus.addAll(toMenuResponse(store.getMenus()));
        response.reviews.addAll(toReviewResponse(reviews, userDictionary));
        response.visitHistories.addAll(toVisitHistoryResponse(visitHistories, userDictionary));
        response.setBaseTime(store);
        return response;
    }

    private static List<VisitHistoryWithUserResponse> toVisitHistoryResponse(List<VisitHistoryWithUserProjection> visitHistories, UserDictionary userCacheCollection) {
        return visitHistories.stream()
            .map(visitHistory -> VisitHistoryWithUserResponse.of(visitHistory, userCacheCollection.getUser(visitHistory.getUserId())))
            .collect(Collectors.toList());
    }

    private static List<ReviewWithUserResponse> toReviewResponse(List<Review> reviews, UserDictionary userCacheCollection) {
        return reviews.stream()
            .map(review -> ReviewWithUserResponse.of(review, userCacheCollection.getUser(review.getUserId())))
            .sorted(Comparator.comparing(ReviewWithUserResponse::getReviewId).reversed())
            .collect(Collectors.toList());
    }

    private static List<MenuResponse> toMenuResponse(List<Menu> menus) {
        return menus.stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }

    private static List<StoreImageResponse> toImageResponse(List<StoreImage> storeImages) {
        return storeImages.stream()
            .map(StoreImageResponse::of)
            .collect(Collectors.toList());
    }

}
