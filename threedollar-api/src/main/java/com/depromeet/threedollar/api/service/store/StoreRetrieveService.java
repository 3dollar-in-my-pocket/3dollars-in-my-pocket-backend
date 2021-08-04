package com.depromeet.threedollar.api.service.store;

import com.depromeet.threedollar.api.service.store.dto.request.RetrieveNearStoresRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveMyStoresRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveStoreGroupByCategoryRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveStoreDetailInfoRequest;
import com.depromeet.threedollar.api.service.store.dto.response.*;
import com.depromeet.threedollar.domain.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.user.User;
import com.depromeet.threedollar.domain.domain.user.UserRepository;
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

    @Transactional(readOnly = true)
    public List<StoreInfoResponse> getNearStores(RetrieveNearStoresRequest request) {
        List<Store> stores = storeRepository.findStoresByLocationLessThanDistance(
            request.getMapLatitude(), request.getMapLongitude(), Math.min(request.getDistance(), LIMIT_DISTANCE)
        );
        return stores.stream()
            .map(store -> StoreInfoResponse.of(store, request.getLatitude(), request.getLongitude()))
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

    /**
     * 스크롤 방식으로 사용자가 작성한 가게 정보를 조회한다. (서버에서 다음 스크롤를 반환해줘야한다. 이떄 더이상 가게가 없는 경우 null을 반환)
     * 쿼리를 두 번 날려서 체크하지 않고, 한 번에 처리하기 위해 요청한 가게 갯수 + 1로 조회해서 마지막 1개의 여부에 따라 다음 스크롤 존재 여부를 확인한다.
     */
    @Transactional(readOnly = true)
    public StoresScrollResponse retrieveMyStores(RetrieveMyStoresRequest request, Long userId) {
        List<Store> currentAndNextScrollStores =
            storeRepository.findAllByUserIdWithScroll(userId, request.getCursor(), request.getSize() + 1);

        if (currentAndNextScrollStores.size() <= request.getSize()) {
            return StoresScrollResponse.newLastScroll(
                currentAndNextScrollStores,
                Objects.requireNonNullElseGet(request.getCachingTotalElements(), () -> storeRepository.findCountsByUserId(userId))
            );
        }

        List<Store> currentScrollStores = currentAndNextScrollStores.subList(0, request.getSize());
        return StoresScrollResponse.of(
            currentScrollStores,
            Objects.requireNonNullElseGet(request.getCachingTotalElements(), () -> storeRepository.findCountsByUserId(userId)),
            currentScrollStores.get(request.getSize() - 1).getId()
        );
    }

    @Transactional(readOnly = true)
    public StoresGroupByDistanceResponse retrieveStoresGroupByDistance(RetrieveStoreGroupByCategoryRequest request) {
        List<StoreInfoResponse> stores = findNearByStores(request.getMapLatitude(), request.getMapLongitude()).stream()
            .filter(store -> store.hasCategory(request.getCategory()))
            .map(store -> StoreInfoResponse.of(store, request.getLatitude(), request.getLongitude()))
            .sorted(Comparator.comparing(StoreInfoResponse::getDistance))
            .collect(Collectors.toList());
        return StoresGroupByDistanceResponse.of(stores);
    }

    @Transactional(readOnly = true)
    public StoresGroupByReviewResponse retrieveStoresGroupByRating(RetrieveStoreGroupByCategoryRequest request) {
        List<StoreInfoResponse> stores = findNearByStores(request.getMapLatitude(), request.getMapLongitude()).stream()
            .filter(store -> store.hasCategory(request.getCategory()))
            .map(store -> StoreInfoResponse.of(store, request.getLatitude(), request.getLongitude()))
            .sorted(Comparator.comparing(StoreInfoResponse::getRating).reversed())
            .collect(Collectors.toList());
        return StoresGroupByReviewResponse.of(stores);
    }

    private List<Store> findNearByStores(double latitude, double longitude) {
        return storeRepository.findStoresByLocationLessThanDistance(latitude, longitude, 2.0);
    }

}
