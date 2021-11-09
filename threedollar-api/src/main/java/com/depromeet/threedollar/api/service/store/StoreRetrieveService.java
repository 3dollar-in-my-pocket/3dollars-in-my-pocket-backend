package com.depromeet.threedollar.api.service.store;

import com.depromeet.threedollar.api.service.store.dto.request.RetrieveMyStoresRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveNearStoresRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveStoreDetailInfoRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveStoreGroupByCategoryRequest;
import com.depromeet.threedollar.api.service.store.dto.response.*;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.review.projection.ReviewWithWriterProjection;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImage;
import com.depromeet.threedollar.domain.domain.storeimage.StoreImageRepository;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoriesCountCollection;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.domain.visit.projection.VisitHistoryWithUserProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StoreRetrieveService {

    private static final double LIMIT_DISTANCE = 2.0;

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional(readOnly = true)
    public List<StoreInfoResponse> getNearStores(RetrieveNearStoresRequest request) {
        List<Store> nearStores = storeRepository.findStoresByLocationLessThanDistance(
            request.getMapLatitude(), request.getMapLongitude(), Math.min(request.getDistance(), LIMIT_DISTANCE)
        );
        VisitHistoriesCountCollection collection = findVisitHistoriesCountByStoreIds(nearStores);
        return nearStores.stream()
            .map(store -> StoreInfoResponse.of(store, request.getLatitude(), request.getLongitude(),
                collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
            .sorted(Comparator.comparing(StoreInfoResponse::getDistance))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StoreDetailResponse getDetailStoreInfo(RetrieveStoreDetailInfoRequest request) {
        Store store = StoreServiceUtils.findStoreByIdFetchJoinMenu(storeRepository, request.getStoreId());
        User creator = userRepository.findUserById(store.getUserId());
        List<StoreImage> images = storeImageRepository.findAllByStoreId(request.getStoreId());
        List<ReviewWithWriterProjection> reviews = reviewRepository.findAllWithCreatorByStoreId(request.getStoreId());
        List<VisitHistoryWithUserProjection> visitHistories = visitHistoryRepository.findAllVisitWithUserByStoreIdBetweenDate(request.getStoreId(), request.getStartDate(), request.getEndDate());
        return StoreDetailResponse.of(store, images, request.getLatitude(), request.getLongitude(), creator, reviews, visitHistories);
    }

    @Transactional(readOnly = true)
    public StoresScrollResponse retrieveMyStores(RetrieveMyStoresRequest request, Long userId) {
        List<Store> currentAndNextScrollStores =
            storeRepository.findAllByUserIdWithScroll(userId, request.getCursor(), request.getSize() + 1);
        VisitHistoriesCountCollection collection = findVisitHistoriesCountByStoreIds(currentAndNextScrollStores);
        long totalElements = Objects.requireNonNullElseGet(request.getCachingTotalElements(), () -> storeRepository.findCountsByUserId(userId));

        if (hasNoMoreNextStore(currentAndNextScrollStores, request.getSize())) {
            return StoresScrollResponse.newLastScroll(currentAndNextScrollStores, collection, totalElements, request.getLatitude(), request.getLongitude());
        }

        List<Store> currentScrollStores = currentAndNextScrollStores.subList(0, request.getSize());
        long nextCursor = currentScrollStores.get(request.getSize() - 1).getId();
        return StoresScrollResponse.newScrollHasNext(currentScrollStores, collection, totalElements, request.getLatitude(), request.getLongitude(), nextCursor);
    }

    private boolean hasNoMoreNextStore(List<Store> hasStores, int size) {
        return hasStores.size() <= size;
    }

    @Transactional(readOnly = true)
    public StoresGroupByDistanceResponse getNearStoresGroupByDistance(RetrieveStoreGroupByCategoryRequest request) {
        List<Store> nearStores = findNearStores(request.getMapLatitude(), request.getMapLongitude());
        List<StoreInfoResponse> nearCategoryStores = filterByCategory(nearStores, request).stream()
            .sorted(Comparator.comparing(StoreInfoResponse::getDistance))
            .collect(Collectors.toList());
        return StoresGroupByDistanceResponse.of(nearCategoryStores);
    }

    @Transactional(readOnly = true)
    public StoresGroupByReviewResponse getNearStoresGroupByReview(RetrieveStoreGroupByCategoryRequest request) {
        List<Store> nearStores = findNearStores(request.getMapLatitude(), request.getMapLongitude());
        List<StoreInfoResponse> nearCategoryStores = filterByCategory(nearStores, request).stream()
            .sorted(Comparator.comparing(StoreInfoResponse::getRating).reversed())
            .collect(Collectors.toList());
        return StoresGroupByReviewResponse.of(nearCategoryStores);
    }

    private List<Store> findNearStores(double latitude, double longitude) {
        return storeRepository.findStoresByLocationLessThanDistance(latitude, longitude, LIMIT_DISTANCE);
    }

    private List<StoreInfoResponse> filterByCategory(List<Store> nearStores, RetrieveStoreGroupByCategoryRequest request) {
        VisitHistoriesCountCollection visitHistoriesCountCollection = findVisitHistoriesCountByStoreIds(nearStores);
        return nearStores.stream()
            .filter(store -> store.hasCategory(request.getCategory()))
            .map(store -> StoreInfoResponse.of(store, request.getLatitude(), request.getLongitude(),
                visitHistoriesCountCollection.getStoreExistsVisitsCount(store.getId()),
                visitHistoriesCountCollection.getStoreNotExistsVisitsCount(store.getId())))
            .collect(Collectors.toList());
    }

    private VisitHistoriesCountCollection findVisitHistoriesCountByStoreIds(List<Store> stores) {
        List<Long> storeIds = stores.stream().distinct()
            .map(Store::getId)
            .collect(Collectors.toList());
        return VisitHistoriesCountCollection.of(visitHistoryRepository.findCountsByStoreIdWithGroup(storeIds));
    }

}
