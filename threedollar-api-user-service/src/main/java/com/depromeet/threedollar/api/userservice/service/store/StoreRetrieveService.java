package com.depromeet.threedollar.api.userservice.service.store;

import static com.depromeet.threedollar.api.userservice.service.store.StoreServiceUtils.findAroundStoresFilerByCategory;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depromeet.threedollar.api.userservice.service.store.dto.request.CheckExistsStoresNearbyRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.RetrieveAroundStoresRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.RetrieveMyStoresRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.RetrieveStoreDetailRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.CheckExistStoresNearbyResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreDetailResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoresCursorResponse;
import com.depromeet.threedollar.common.model.LocationValue;
import com.depromeet.threedollar.domain.rds.collection.userservice.user.UserDictionary;
import com.depromeet.threedollar.domain.rds.collection.userservice.visit.VisitHistoryCounter;
import com.depromeet.threedollar.domain.rds.common.support.CursorPagingSupporter;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.Review;
import com.depromeet.threedollar.domain.rds.domain.userservice.review.ReviewRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.Store;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreImageProjection;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.projection.StoreWithMenuProjection;
import com.depromeet.threedollar.domain.rds.domain.userservice.user.UserRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.VisitHistoryRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.visit.projection.VisitHistoryWithUserProjection;
import com.depromeet.threedollar.domain.redis.domain.userservice.store.AroundUserStoresCacheRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StoreRetrieveService {

    private final StoreRepository storeRepository;
    private final AroundUserStoresCacheRepository aroundUserStoresCacheRepository;

    private final StoreImageRepository storeImageRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final VisitHistoryRepository visitHistoryRepository;

    @Transactional(readOnly = true)
    public List<StoreWithVisitsAndDistanceResponse> retrieveAroundStores(RetrieveAroundStoresRequest request, LocationValue deviceLocation, LocationValue mapLocation) {
        List<StoreInfoResponse> aroundStoresFilerByCategory = findAroundStoresFilerByCategory(storeRepository, aroundUserStoresCacheRepository, mapLocation.getLatitude(), mapLocation.getLongitude(), request.getDistance(), request.getCategory());
        VisitHistoryCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(aroundStoresFilerByCategory.stream()
            .map(StoreInfoResponse::getStoreId)
            .collect(Collectors.toList()));
        return aroundStoresFilerByCategory.stream()
            .map(store -> StoreWithVisitsAndDistanceResponse.of(store, deviceLocation, visitHistoriesCounter))
            .sorted(request.getSorted())
            .limit(request.getSize())
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StoreDetailResponse retrieveStoreDetailInfo(RetrieveStoreDetailRequest request, LocationValue deviceLocation) {
        Store store = StoreServiceUtils.findStoreByIdFetchJoinMenu(storeRepository, request.getStoreId());
        List<Review> reviews = reviewRepository.findAllByStoreId(request.getStoreId());
        List<StoreImageProjection> storeImages = storeImageRepository.findAllByStoreId(request.getStoreId());
        List<VisitHistoryWithUserProjection> visitHistories = visitHistoryRepository.findAllVisitWithUserByStoreIdAfterDate(request.getStoreId(), request.getStartDate());
        UserDictionary userDictionary = UserDictionary.of(userRepository.findAllByUserId(concatUserIds(reviews, visitHistories, store)));
        VisitHistoryCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(List.of(store.getId()));
        return StoreDetailResponse.of(store, deviceLocation, storeImages, userDictionary, reviews, visitHistoriesCounter, visitHistories);
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
        List<StoreWithMenuProjection> storesWithNextCursor = storeRepository.findAllByUserIdUsingCursor(userId, request.getCursor(), request.getSize() + 1);
        CursorPagingSupporter<StoreWithMenuProjection> storesCursor = CursorPagingSupporter.of(storesWithNextCursor, request.getSize());
        VisitHistoryCounter visitHistoriesCounter = findVisitHistoriesCountByStoreIdsInDuration(storesCursor.getCurrentCursorItems().stream()
            .map(StoreWithMenuProjection::getId)
            .collect(Collectors.toList())
        );
        return StoresCursorResponse.of(storesCursor, visitHistoriesCounter, storeRepository.countByUserId(userId));
    }

    private VisitHistoryCounter findVisitHistoriesCountByStoreIdsInDuration(List<Long> storeIds) {
        LocalDate monthAgo = LocalDate.now().minusMonths(1);
        return VisitHistoryCounter.of(visitHistoryRepository.countGroupingByStoreId(storeIds, monthAgo));
    }

    @Transactional(readOnly = true)
    public CheckExistStoresNearbyResponse checkExistStoresNearby(CheckExistsStoresNearbyRequest request, LocationValue mapLocation) {
        boolean isExists = storeRepository.existsStoreAroundInDistance(mapLocation.getLatitude(), mapLocation.getLongitude(), request.getDistance());
        return CheckExistStoresNearbyResponse.of(isExists);
    }

}