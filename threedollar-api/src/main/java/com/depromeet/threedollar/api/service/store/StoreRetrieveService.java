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
import com.depromeet.threedollar.domain.collection.common.ScrollPaginationCollection;
import com.depromeet.threedollar.domain.collection.user.UserCacheCollection;
import com.depromeet.threedollar.domain.collection.visit.VisitHistoryCounter;
import com.depromeet.threedollar.domain.domain.review.Review;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRadiusDistance;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImageRepository;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.depromeet.threedollar.api.service.store.StoreServiceUtils.findNearStoresFilterByCategory;

@RequiredArgsConstructor
@Service
public class StoreRetrieveService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional(readOnly = true)
    public List<StoreWithVisitsAndDistanceResponse> getNearStores(RetrieveNearStoresRequest request) {
        List<Store> nearStores = findNearStoresFilterByCategory(storeRepository, request.getMapLatitude(), request.getMapLongitude(), request.getDistance(), request.getCategory());
        VisitHistoryCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(nearStores);
        return nearStores.stream()
            .map(store -> StoreWithVisitsAndDistanceResponse.of(store, request.getLatitude(), request.getLongitude(), visitHistoriesCounter))
            .sorted(request.getSorted())
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StoreDetailResponse getDetailStoreInfo(RetrieveStoreDetailRequest request) {
        Store store = StoreServiceUtils.findStoreByIdFetchJoinMenu(storeRepository, request.getStoreId());
        List<Review> reviews = reviewRepository.findAllByStoreId(request.getStoreId());
        List<StoreImage> storeImages = storeImageRepository.findAllByStoreId(request.getStoreId());
        List<VisitHistoryWithUserProjection> visitHistories = visitHistoryRepository.findAllVisitWithUserByStoreIdAfterDate(request.getStoreId(), request.getStartDate());
        UserCacheCollection users = UserCacheCollection.of(userRepository.findAllByUserId(assembleUserIds(reviews, visitHistories, store)));
        VisitHistoryCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(Collections.singletonList(store));
        return StoreDetailResponse.of(store, request.getLatitude(), request.getLongitude(), storeImages, users, reviews, visitHistoriesCounter, visitHistories);
    }

    private List<Long> assembleUserIds(List<Review> reviews, List<VisitHistoryWithUserProjection> visitHistories, Store store) {
        return Stream.of(reviews.stream()
                .map(Review::getUserId)
                .collect(Collectors.toList()),
            visitHistories.stream()
                .map(VisitHistoryWithUserProjection::getUserId)
                .collect(Collectors.toList()),
            List.of(store.getUserId())
        )
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StoresScrollResponse retrieveMyReportedStoreHistories(RetrieveMyStoresRequest request, Long userId) {
        List<Store> storesWithNextCursor = storeRepository.findAllByUserIdWithScroll(userId, request.getCursor(), request.getSize() + 1);
        ScrollPaginationCollection<Store> storesScroll = ScrollPaginationCollection.of(storesWithNextCursor, request.getSize());
        VisitHistoryCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(storesScroll.getCurrentScrollItems());
        return StoresScrollResponse.of(storesScroll, visitHistoriesCounter, storeRepository.findCountsByUserId(userId));
    }

    @Deprecated
    @Transactional(readOnly = true)
    public StoresScrollV2Response retrieveMyReportedStoreHistoriesV2(RetrieveMyStoresV2Request request, Long userId) {
        List<Store> storesWithNextCursor = storeRepository.findAllActiveByUserIdWithScroll(userId, request.getCursor(), request.getSize() + 1);
        ScrollPaginationCollection<Store> storesScroll = ScrollPaginationCollection.of(storesWithNextCursor, request.getSize());
        VisitHistoryCounter visitHistoryCountsCollection = findVisitHistoriesCountByStoreIdsInDuration(storesScroll.getCurrentScrollItems());
        long totalElements = Objects.requireNonNullElseGet(request.getCachingTotalElements(), () -> storeRepository.findActiveCountsByUserId(userId));
        return StoresScrollV2Response.of(storesScroll, visitHistoryCountsCollection, totalElements, request.getLatitude(), request.getLongitude());
    }

    @Deprecated
    @Transactional(readOnly = true)
    public StoresGroupByDistanceV2Response getNearStoresGroupByDistance(RetrieveStoreGroupByCategoryV2Request request) {
        List<Store> nearStores = findNearStoresFilterByCategory(storeRepository, request.getMapLatitude(), request.getMapLongitude(), StoreRadiusDistance.max(), request.getCategory());
        VisitHistoryCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(nearStores);
        List<StoreWithVisitsAndDistanceResponse> nearCategoryStores = nearStores.stream()
            .map(store -> StoreWithVisitsAndDistanceResponse.of(store, request.getLatitude(), request.getLongitude(), visitHistoriesCounter))
            .sorted(StoreOrderType.DISTANCE_ASC.getSorted())
            .collect(Collectors.toList());
        return StoresGroupByDistanceV2Response.of(nearCategoryStores);
    }

    @Deprecated
    @Transactional(readOnly = true)
    public StoresGroupByReviewV2Response getNearStoresGroupByReview(RetrieveStoreGroupByCategoryV2Request request) {
        List<Store> nearStores = findNearStoresFilterByCategory(storeRepository, request.getMapLatitude(), request.getMapLongitude(), StoreRadiusDistance.max(), request.getCategory());
        VisitHistoryCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(nearStores);
        List<StoreWithVisitsAndDistanceResponse> nearCategoryStores = nearStores.stream()
            .map(store -> StoreWithVisitsAndDistanceResponse.of(store, request.getLatitude(), request.getLongitude(), visitHistoriesCounter))
            .sorted(StoreOrderType.REVIEW_DESC.getSorted())
            .collect(Collectors.toList());
        return StoresGroupByReviewV2Response.of(nearCategoryStores);
    }

    private VisitHistoryCounter findVisitHistoriesCountByStoreIdsInDuration(List<Store> stores) {
        LocalDate monthAgo = LocalDate.now().minusMonths(1);
        List<Long> storeIds = stores.stream()
            .map(Store::getId).distinct()
            .collect(Collectors.toList());
        return VisitHistoryCounter.of(visitHistoryRepository.findCountsByStoreIdWithGroup(storeIds, monthAgo));
    }

}
