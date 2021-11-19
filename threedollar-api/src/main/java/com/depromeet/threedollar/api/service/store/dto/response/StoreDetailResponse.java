package com.depromeet.threedollar.api.service.store.dto.response;

import com.depromeet.threedollar.api.service.storeimage.dto.response.StoreImageResponse;
import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryCountsResponse;
import com.depromeet.threedollar.api.service.visit.dto.response.VisitHistoryWithUserResponse;
import com.depromeet.threedollar.application.common.dto.AuditingTimeResponse;
import com.depromeet.threedollar.api.service.review.dto.response.ReviewWithUserResponse;
import com.depromeet.threedollar.api.service.user.dto.response.UserInfoResponse;
import com.depromeet.threedollar.common.utils.LocationDistanceUtils;
import com.depromeet.threedollar.domain.domain.common.DayOfTheWeek;
import com.depromeet.threedollar.domain.domain.menu.Menu;
import com.depromeet.threedollar.domain.domain.menu.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.store.PaymentMethodType;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreType;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoriesCountCollection;
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

    public static StoreDetailResponse of(Store store, List<StoreImage> storeImages, double latitude,
                                         double longitude, User user, List<ReviewWithWriterProjection> reviews,
                                         VisitHistoriesCountCollection visitHistoriesCollection, List<VisitHistoryWithUserProjection> visitHistories) {
        StoreDetailResponse response = StoreDetailResponse.builder()
            .storeId(store.getId())
            .latitude(store.getLatitude())
            .longitude(store.getLongitude())
            .storeName(store.getName())
            .storeType(store.getType())
            .rating(store.getRating())
            .distance(LocationDistanceUtils.getDistance(store.getLatitude(), store.getLongitude(), latitude, longitude))
            .user(UserInfoResponse.of(user))
            .visitHistory(VisitHistoryCountsResponse.of(visitHistoriesCollection.getStoreExistsVisitsCount(store.getId()), visitHistoriesCollection.getStoreNotExistsVisitsCount(store.getId())))
            .build();
        response.categories.addAll(store.getMenuCategoriesSortedByCounts());
        response.appearanceDays.addAll(store.getAppearanceDayTypes());
        response.paymentMethods.addAll(store.getPaymentMethodTypes());
        response.images.addAll(toImageResponse(storeImages));
        response.menus.addAll(toMenuResponse(store.getMenus()));
        response.reviews.addAll(toReviewResponse(reviews));
        response.visitHistories.addAll(toVisitHistoryResponse(visitHistories));
        response.setBaseTime(store);
        return response;
    }

    private static Collection<VisitHistoryWithUserResponse> toVisitHistoryResponse(List<VisitHistoryWithUserProjection> visitHistories) {
        return visitHistories.stream()
            .map(VisitHistoryWithUserResponse::of)
            .collect(Collectors.toList());
    }

    private static List<ReviewWithUserResponse> toReviewResponse(List<ReviewWithWriterProjection> reviews) {
        return reviews.stream()
            .map(ReviewWithUserResponse::of)
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
