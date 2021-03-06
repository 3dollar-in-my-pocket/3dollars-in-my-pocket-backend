package com.depromeet.threedollar.api.userservice.service.store.dto.response;

import com.depromeet.threedollar.api.core.service.common.dto.response.AuditingTimeResponse;
import com.depromeet.threedollar.api.userservice.service.review.dto.response.ReviewWithUserResponse;
import com.depromeet.threedollar.api.userservice.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.api.userservice.service.visit.dto.response.VisitHistoryCountsResponse;
import com.depromeet.threedollar.api.userservice.service.visit.dto.response.VisitHistoryWithUserResponse;
import com.depromeet.threedollar.common.model.LocationValue;
import com.depromeet.threedollar.common.type.DayOfTheWeek;
import com.depromeet.threedollar.common.type.UserMenuCategoryType;
import com.depromeet.threedollar.common.utils.distance.LocationDistanceUtils;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Menu;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.PaymentMethodType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreType;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreImageProjection;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.collection.UserDictionary;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.collection.VisitHistoryCountDictionary;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.projection.VisitHistoryWithUserProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StoreDetailResponse extends AuditingTimeResponse {

    // ??????
    private Long storeId;

    private double latitude;

    private double longitude;

    private String storeName;

    @Nullable
    private StoreType storeType;

    private double rating;

    private int distance;

    private final List<UserMenuCategoryType> categories = new ArrayList<>();

    private final Set<DayOfTheWeek> appearanceDays = new HashSet<>();

    private final Set<PaymentMethodType> paymentMethods = new HashSet<>();

    private final List<MenuResponse> menus = new ArrayList<>();

    // ?????? ?????????
    private final List<StoreImageResponse> images = new ArrayList<>();

    // ??????
    private final List<ReviewWithUserResponse> reviews = new ArrayList<>();

    // ?????????
    private UserInfoResponse user;

    // ?????? ??????
    private VisitHistoryCountsResponse visitHistory;

    private final List<VisitHistoryWithUserResponse> visitHistories = new ArrayList<>();

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

    public static StoreDetailResponse of(Store store, LocationValue deviceLocation, List<StoreImageProjection> storeImages, UserDictionary userDictionary,
                                         List<Review> reviews, VisitHistoryCountDictionary visitHistoriesCollection, List<VisitHistoryWithUserProjection> visitHistories) {
        StoreDetailResponse response = StoreDetailResponse.builder()
            .store(store)
            .distance(LocationDistanceUtils.getDistanceM(LocationValue.of(store.getLatitude(), store.getLongitude()), deviceLocation))
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

    private static List<StoreImageResponse> toImageResponse(List<StoreImageProjection> storeImages) {
        return storeImages.stream()
            .map(StoreImageResponse::of)
            .collect(Collectors.toList());
    }

}
