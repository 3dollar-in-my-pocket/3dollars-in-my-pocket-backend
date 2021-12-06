package com.depromeet.threedollar.api.service.store;

import com.depromeet.threedollar.api.service.store.dto.request.RetrieveMyStoresRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveNearStoresRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveStoreDetailRequest;
import com.depromeet.threedollar.api.service.store.dto.request.deprecated.RetrieveMyStoresV2Request;
import com.depromeet.threedollar.api.service.store.dto.request.deprecated.RetrieveStoreGroupByCategoryV2Request;
import com.depromeet.threedollar.api.service.store.dto.response.StoreDetailResponse;
import com.depromeet.threedollar.api.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import com.depromeet.threedollar.api.service.store.dto.response.StoresScrollResponse;
import com.depromeet.threedollar.api.service.store.dto.response.deprecated.StoresGroupByDistanceV2Response;
import com.depromeet.threedollar.api.service.store.dto.response.deprecated.StoresGroupByReviewV2Response;
import com.depromeet.threedollar.api.service.store.dto.response.deprecated.StoresScrollV2Response;
import com.depromeet.threedollar.api.service.store.dto.type.StoreOrderType;
import com.depromeet.threedollar.common.collection.ScrollPaginationCollection;
import com.depromeet.threedollar.domain.domain.medal.UserMedalsCollection;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.store.MenuCategoryType;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImageRepository;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.visit.collection.VisitHistoriesCounter;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class StoreRetrieveService {

    private static final double MAX_RADIUS_DISTANCE = 2.0;

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional(readOnly = true)
    public List<StoreWithVisitsAndDistanceResponse> getNearStores(RetrieveNearStoresRequest request) {
        List<Store> nearStores = findNearStoresFilterByCategory(request.getMapLatitude(), request.getMapLongitude(), request.getDistance(), request.getCategory());
        VisitHistoriesCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(nearStores);
        return nearStores.stream()
            .map(store -> StoreWithVisitsAndDistanceResponse.of(store, request.getLatitude(), request.getLongitude(), visitHistoriesCounter))
            .sorted(request.getSorted())
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StoreDetailResponse getDetailStoreInfo(RetrieveStoreDetailRequest request) {
        Store store = StoreServiceUtils.findStoreByIdFetchJoinMenu(storeRepository, request.getStoreId());
        User creator = userRepository.findUserById(store.getUserId());
        List<StoreImage> images = storeImageRepository.findAllByStoreId(request.getStoreId());
        List<ReviewWithWriterProjection> reviews = reviewRepository.findAllWithCreatorByStoreId(request.getStoreId());
        List<VisitHistoryWithUserProjection> visitHistories = visitHistoryRepository.findAllVisitWithUserByStoreIdAfterDate(request.getStoreId(), request.getStartDate());
        VisitHistoriesCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(Collections.singletonList(store));
        UserMedalsCollection userMedalCollection = findActiveMedalByUserIds(reviews, visitHistories);
        return StoreDetailResponse.of(store, images, request.getLatitude(), request.getLongitude(), creator, reviews, visitHistoriesCounter, visitHistories, userMedalCollection);
    }

    @Transactional(readOnly = true)
    public StoresScrollResponse retrieveMyReportedStoreHistories(RetrieveMyStoresRequest request, Long userId) {
        List<Store> storesWithNextCursor = storeRepository.findAllByUserIdWithScroll(userId, request.getCursor(), request.getSize() + 1);
        ScrollPaginationCollection<Store> scrollCollection = ScrollPaginationCollection.of(storesWithNextCursor, request.getSize());
        VisitHistoriesCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(scrollCollection.getItemsInCurrentScroll());
        return StoresScrollResponse.of(scrollCollection, visitHistoriesCounter, storeRepository.findCountsByUserId(userId));
    }

    @Deprecated
    @Transactional(readOnly = true)
    public StoresScrollV2Response retrieveMyReportedStoreHistoriesV2(RetrieveMyStoresV2Request request, Long userId) {
        List<Store> storesWithNextCursor = storeRepository.findAllActiveByUserIdWithScroll(userId, request.getCursor(), request.getSize() + 1);
        ScrollPaginationCollection<Store> scrollCollection = ScrollPaginationCollection.of(storesWithNextCursor, request.getSize());
        VisitHistoriesCounter visitHistoryCountsCollection = findVisitHistoriesCountByStoreIdsInDuration(scrollCollection.getItemsInCurrentScroll());
        long totalElements = Objects.requireNonNullElseGet(request.getCachingTotalElements(), () -> storeRepository.findActiveCountsByUserId(userId));
        return StoresScrollV2Response.of(scrollCollection, visitHistoryCountsCollection, totalElements, request.getLatitude(), request.getLongitude());
    }

    @Deprecated
    @Transactional(readOnly = true)
    public StoresGroupByDistanceV2Response getNearStoresGroupByDistance(RetrieveStoreGroupByCategoryV2Request request) {
        List<Store> nearStores = findNearStoresFilterByCategory(request.getMapLatitude(), request.getMapLongitude(), MAX_RADIUS_DISTANCE, request.getCategory());
        VisitHistoriesCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(nearStores);
        List<StoreWithVisitsAndDistanceResponse> nearCategoryStores = nearStores.stream()
            .map(store -> StoreWithVisitsAndDistanceResponse.of(store, request.getLatitude(), request.getLongitude(), visitHistoriesCounter))
            .sorted(StoreOrderType.DISTANCE_ASC.getSorted())
            .collect(Collectors.toList());
        return StoresGroupByDistanceV2Response.of(nearCategoryStores);
    }

    @Deprecated
    @Transactional(readOnly = true)
    public StoresGroupByReviewV2Response getNearStoresGroupByReview(RetrieveStoreGroupByCategoryV2Request request) {
        List<Store> nearStores = findNearStoresFilterByCategory(request.getMapLatitude(), request.getMapLongitude(), MAX_RADIUS_DISTANCE, request.getCategory());
        VisitHistoriesCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(nearStores);
        List<StoreWithVisitsAndDistanceResponse> nearCategoryStores = nearStores.stream()
            .map(store -> StoreWithVisitsAndDistanceResponse.of(store, request.getLatitude(), request.getLongitude(), visitHistoriesCounter))
            .sorted(StoreOrderType.REVIEW_DESC.getSorted())
            .collect(Collectors.toList());
        return StoresGroupByReviewV2Response.of(nearCategoryStores);
    }

    private List<Store> findNearStoresFilterByCategory(double mapLatitude, double mapLongitude, double distance, @Nullable MenuCategoryType categoryType) {
        List<Store> nearStores = storeRepository.findStoresByLocationLessThanDistance(mapLatitude, mapLongitude, Math.min(distance, MAX_RADIUS_DISTANCE));
        if (categoryType == null) {
            return nearStores;
        }
        return nearStores.stream()
            .filter(store -> store.hasMenuCategory(categoryType))
            .collect(Collectors.toList());
    }

    private VisitHistoriesCounter findVisitHistoriesCountByStoreIdsInDuration(List<Store> stores) {
        LocalDate monthAgo = LocalDate.now().minusMonths(1);
        List<Long> storeIds = stores.stream()
            .map(Store::getId).distinct()
            .collect(Collectors.toList());
        return VisitHistoriesCounter.of(visitHistoryRepository.findCountsByStoreIdWithGroup(storeIds, monthAgo));
    }

    private UserMedalsCollection findActiveMedalByUserIds(List<ReviewWithWriterProjection> reviews, List<VisitHistoryWithUserProjection> visitHistories) {
        List<Long> distinctUserIds = getDistinctUserIds(reviews, visitHistories);
        return UserMedalsCollection.of(userRepository.findAllByUserId(distinctUserIds));
    }

    private List<Long> getDistinctUserIds(List<ReviewWithWriterProjection> reviews, List<VisitHistoryWithUserProjection> visitHistories) {
        return Stream.concat(reviews.stream()
                .map(ReviewWithWriterProjection::getUserId)
                .collect(Collectors.toList()).stream(),
            visitHistories.stream()
                .map(VisitHistoryWithUserProjection::getUserId)
                .collect(Collectors.toList()).stream())
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
    }

}
