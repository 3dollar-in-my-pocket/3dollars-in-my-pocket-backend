package com.depromeet.threedollar.api.user.service.store.dto.response;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.depromeet.threedollar.api.core.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.api.user.service.review.dto.response.ReviewWithUserResponse;
import com.depromeet.threedollar.api.user.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.api.user.service.visit.dto.response.VisitHistoryCountsResponse;
import com.depromeet.threedollar.api.user.service.visit.dto.response.VisitHistoryWithUserResponse;
import com.depromeet.threedollar.common.model.CoordinateValue;
import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.common.utils.LocationDistanceUtils;
import com.depromeet.threedollar.domain.rds.user.collection.user.UserDictionary;
import com.depromeet.threedollar.domain.rds.user.collection.visit.VisitHistoryCounter;
import com.depromeet.threedollar.domain.rds.user.domain.review.Review;
import com.depromeet.threedollar.domain.rds.user.domain.store.Menu;
import com.depromeet.threedollar.domain.rds.user.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.rds.user.domain.store.PaymentMethodType;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreType;
import com.depromeet.threedollar.domain.rds.user.domain.visit.projection.VisitHistoryWithUserProjection;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreDetailResponse extends AuditingTimeResponse {

    private final List<MenuCategoryType> categories = new ArrayList<>();
    private final Set<DayOfTheWeek> appearanceDays = new HashSet<>();
    private final Set<PaymentMethodType> paymentMethods = new HashSet<>();
    private final List<MenuResponse> menus = new ArrayList<>();
    // 가게 이미지
    private final List<StoreImageResponse> images = new ArrayList<>();
    // 리뷰
    private final List<ReviewWithUserResponse> reviews = new ArrayList<>();
    private final List<VisitHistoryWithUserResponse> visitHistories = new ArrayList<>();
    // 가게
    private Long storeId;
    private double latitude;
    private double longitude;
    private String storeName;
    @Nullable
    private StoreType storeType;
    private double rating;
    private int distance;
    // 작성자
    private UserInfoResponse user;
    // 방문 인증
    private VisitHistoryCountsResponse visitHistory;

    @Builder(access = AccessLevel.PRIVATE)
    private StoreDetailResponse(Store store, int distance, UserInfoResponse user, VisitHistoryCountsResponse visitHistory) {
        this.storeId = store.getId();
        this.latitude = store.getLatitude();
        this.longitude = store.getLongitude();
        this.storeName = store.getName();
        this.storeType = store.getType();
        this.rating = store.getRating();
        this.distance = distance;
        this.user = user;
        this.visitHistory = visitHistory;
    }

    public static StoreDetailResponse of(Store store, CoordinateValue geoCoordinate, List<StoreImage> storeImages, UserDictionary userDictionary,
                                         List<Review> reviews, VisitHistoryCounter visitHistoriesCollection, List<VisitHistoryWithUserProjection> visitHistories) {
        StoreDetailResponse response = StoreDetailResponse.builder()
            .store(store)
            .distance(LocationDistanceUtils.getDistance(CoordinateValue.of(store.getLatitude(), store.getLongitude()), geoCoordinate))
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
        response.setAuditingTimeByEntity(store);
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
