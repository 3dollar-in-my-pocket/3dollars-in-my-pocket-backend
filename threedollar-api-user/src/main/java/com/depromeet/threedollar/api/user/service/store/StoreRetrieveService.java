package com.depromeet.threedollar.api.user.service.store;

import static com.depromeet.threedollar.api.user.service.store.StoreServiceUtils.findNearStoresFilterByCategory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.threedollar.api.user.service.store.dto.request.CheckExistsStoresNearbyRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.RetrieveMyStoresRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.RetrieveNearStoresRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.RetrieveStoreDetailRequest;
import com.depromeet.threedollar.api.user.service.store.dto.response.CheckExistStoresNearbyResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreDetailResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoresCursorResponse;
import com.depromeet.threedollar.common.model.CoordinateValue;
import com.depromeet.threedollar.domain.rds.common.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.user.collection.user.UserDictionary;
import com.depromeet.threedollar.domain.rds.user.collection.visit.VisitHistoryCounter;
import com.depromeet.threedollar.domain.rds.user.domain.review.Review;
import com.depromeet.threedollar.domain.rds.user.domain.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImage;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreImageRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.user.domain.user.UserRepository;
import com.depromeet.threedollar.domain.rds.user.domain.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.rds.user.domain.visit.projection.VisitHistoryWithUserProjection;
import com.depromeet.threedollar.domain.redis.domain.user.store.CachedAroundStoreRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreRetrieveService {

    private final StoreRepository storeRepository;
    private final CachedAroundStoreRepository cachedAroundStoreRepository;

    private final StoreImageRepository storeImageRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional(readOnly = true)
    public List<StoreWithVisitsAndDistanceResponse> getNearStores(RetrieveNearStoresRequest request, CoordinateValue geoCoordinate, CoordinateValue mapCoordinate) {
        List<StoreInfoResponse> nearStores = findNearStoresFilterByCategory(storeRepository, cachedAroundStoreRepository, mapCoordinate.getLatitude(), mapCoordinate.getLongitude(), request.getDistance().getAvailableDistance(), request.getCategory());
        VisitHistoryCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(nearStores.stream()
            .map(StoreInfoResponse::getStoreId)
            .collect(Collectors.toList()));
        return nearStores.stream()
            .map(store -> StoreWithVisitsAndDistanceResponse.of(store, geoCoordinate, visitHistoriesCounter))
            .sorted(request.getSorted())
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StoreDetailResponse getDetailStoreInfo(RetrieveStoreDetailRequest request, CoordinateValue geoCoordinate) {
        Store store = StoreServiceUtils.findStoreByIdFetchJoinMenu(storeRepository, request.getStoreId());
        List<Review> reviews = reviewRepository.findAllByStoreId(request.getStoreId());
        List<StoreImage> storeImages = storeImageRepository.findAllByStoreId(request.getStoreId());
        List<VisitHistoryWithUserProjection> visitHistories = visitHistoryRepository.findAllVisitWithUserByStoreIdAfterDate(request.getStoreId(), request.getStartDate());
        UserDictionary userDictionary = UserDictionary.of(userRepository.findAllByUserId(concatUserIds(reviews, visitHistories, store)));
        VisitHistoryCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(List.of(store.getId()));
        return StoreDetailResponse.of(store, geoCoordinate, storeImages, userDictionary, reviews, visitHistoriesCounter, visitHistories);
    }

    private List<Long> concatUserIds(List<Review> reviews, List<VisitHistoryWithUserProjection> visitHistories, Store store) {
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
    public StoresCursorResponse retrieveMyReportedStoreHistories(RetrieveMyStoresRequest request, Long userId) {
        List<Store> storesWithNextCursor = storeRepository.findAllByUserIdUsingCursor(userId, request.getCursor(), request.getSize() + 1);
        CursorPagingSupporter<Store> storesCursor = CursorPagingSupporter.of(storesWithNextCursor, request.getSize());
        VisitHistoryCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(storesCursor.getCurrentCursorItems().stream()
            .map(Store::getId)
            .collect(Collectors.toList())
        );
        return StoresCursorResponse.of(storesCursor, visitHistoriesCounter, storeRepository.countByUserId(userId));
    }

    private VisitHistoryCounter findVisitHistoriesCountByStoreIdsInDuration(List<Long> storeIds) {
        LocalDate monthAgo = LocalDate.now().minusMonths(1);
        return VisitHistoryCounter.of(visitHistoryRepository.countGroupingByStoreId(storeIds, monthAgo));
    }

    @Transactional(readOnly = true)
    public CheckExistStoresNearbyResponse checkExistStoresNearby(CheckExistsStoresNearbyRequest request, CoordinateValue mapCoordinate) {
        boolean isExists = storeRepository.existsStoreAroundInDistance(mapCoordinate.getLatitude(), mapCoordinate.getLongitude(), request.getDistance().getAvailableDistance());
        return CheckExistStoresNearbyResponse.of(isExists);
    }

}
