package com.depromeet.threedollar.api.service.store;

import com.depromeet.threedollar.api.service.store.dto.request.*;
import com.depromeet.threedollar.api.service.store.dto.response.*;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoriesCountCollection;
import com.depromeet.threedollar.domain.domain.visit.VisitHistoryRepository;
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

    private final StoreImageService storeImageService;

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional(readOnly = true)
    public List<StoreInfoResponse> getNearStores(RetrieveNearStoresRequest request) {
        List<Store> stores = storeRepository.findStoresByLocationLessThanDistance(
            request.getMapLatitude(), request.getMapLongitude(), Math.min(request.getDistance(), LIMIT_DISTANCE)
        );
        VisitHistoriesCountCollection collection = findVisitHistoriesCountByStoreIds(stores);
        return stores.stream()
            .map(store -> StoreInfoResponse.of(store, request.getLatitude(), request.getLongitude(),
                collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
            .sorted(Comparator.comparing(StoreInfoResponse::getDistance))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StoreDetailResponse getDetailStoreInfo(RetrieveStoreDetailInfoRequest request) {
        Store store = StoreServiceUtils.findStoreByIdFetchJoinMenu(storeRepository, request.getStoreId());
        User creator = userRepository.findUserById(store.getUserId());
        return StoreDetailResponse.of(store, storeImageService.getStoreImages(request.getStoreId()), request.getLatitude(),
            request.getLongitude(), creator, reviewRepository.findAllWithCreatorByStoreId(request.getStoreId()));
    }

    @Transactional(readOnly = true)
    public List<StoreImageResponse> retrieveStoreImages(Long storeId) {
        StoreServiceUtils.validateExistsStore(storeRepository, storeId);
        return storeImageService.getStoreImages(storeId);
    }

    /**
     * 스크롤 방식으로 사용자가 작성한 가게 정보를 조회한다. 요청한 가게 갯수 + 1로 조회해서 마지막 1개의 여부에 따라 다음 스크롤 존재 여부를 확인한다.
     */
    @Transactional(readOnly = true)
    public StoresScrollResponse retrieveMyStores(RetrieveMyStoresRequest request, Long userId) {
        List<Store> currentAndNextScrollStores =
            storeRepository.findAllByUserIdWithScroll(userId, request.getCursor(), request.getSize() + 1);
        VisitHistoriesCountCollection collection = findVisitHistoriesCountByStoreIds(currentAndNextScrollStores);

        if (currentAndNextScrollStores.size() <= request.getSize()) {
            return StoresScrollResponse.newLastScroll(
                currentAndNextScrollStores,
                collection,
                Objects.requireNonNullElseGet(request.getCachingTotalElements(), () -> storeRepository.findCountsByUserId(userId)),
                request.getLatitude(),
                request.getLongitude()
            );
        }

        List<Store> currentScrollStores = currentAndNextScrollStores.subList(0, request.getSize());
        return StoresScrollResponse.of(
            currentScrollStores,
            collection,
            Objects.requireNonNullElseGet(request.getCachingTotalElements(), () -> storeRepository.findCountsByUserId(userId)),
            currentScrollStores.get(request.getSize() - 1).getId(),
            request.getLatitude(),
            request.getLongitude()
        );
    }

    @Transactional(readOnly = true)
    public StoresGroupByDistanceResponse retrieveStoresGroupByDistance(RetrieveStoreGroupByCategoryRequest request) {
        List<Store> findStores = findNearByStores(request.getMapLatitude(), request.getMapLongitude());
        VisitHistoriesCountCollection collection = findVisitHistoriesCountByStoreIds(findStores);
        List<StoreInfoResponse> stores = findNearByStores(request.getMapLatitude(), request.getMapLongitude()).stream()
            .filter(store -> store.hasCategory(request.getCategory()))
            .map(store -> StoreInfoResponse.of(store, request.getLatitude(), request.getLongitude(),
                collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
            .sorted(Comparator.comparing(StoreInfoResponse::getDistance))
            .collect(Collectors.toList());
        return StoresGroupByDistanceResponse.of(stores);
    }

    @Transactional(readOnly = true)
    public StoresGroupByReviewResponse retrieveStoresGroupByRating(RetrieveStoreGroupByCategoryRequest request) {
        List<Store> findStores = findNearByStores(request.getMapLatitude(), request.getMapLongitude());
        VisitHistoriesCountCollection collection = findVisitHistoriesCountByStoreIds(findStores);
        List<StoreInfoResponse> stores = findStores.stream()
            .filter(store -> store.hasCategory(request.getCategory()))
            .map(store -> StoreInfoResponse.of(store, request.getLatitude(), request.getLongitude(),
                collection.getStoreExistsVisitsCount(store.getId()), collection.getStoreNotExistsVisitsCount(store.getId())))
            .sorted(Comparator.comparing(StoreInfoResponse::getRating).reversed())
            .collect(Collectors.toList());
        return StoresGroupByReviewResponse.of(stores);
    }

    private VisitHistoriesCountCollection findVisitHistoriesCountByStoreIds(List<Store> stores) {
        List<Long> storeIds = stores.stream().distinct()
            .map(Store::getId)
            .collect(Collectors.toList());
        return VisitHistoriesCountCollection.of(visitHistoryRepository.findCountsByStoreIdWithGroup(storeIds));
    }

    private List<Store> findNearByStores(double latitude, double longitude) {
        return storeRepository.findStoresByLocationLessThanDistance(latitude, longitude, 2.0);
    }

}
