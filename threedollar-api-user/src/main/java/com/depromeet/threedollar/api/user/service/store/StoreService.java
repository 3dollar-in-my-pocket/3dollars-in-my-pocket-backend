package com.depromeet.threedollar.api.user.service.store;

import com.depromeet.threedollar.api.user.service.store.dto.request.DeleteStoreRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.RegisterStoreRequest;
import com.depromeet.threedollar.api.user.service.store.dto.request.UpdateStoreRequest;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreDeleteResponse;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.domain.rds.user.domain.store.Store;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.rds.user.domain.store.StoreDeleteRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.CONFLICT_DELETE_REQUEST_STORE;
import static com.depromeet.threedollar.common.type.CacheType.CacheKey.USER_STORES_COUNTS;

@RequiredArgsConstructor
@Service
public class StoreService {

    /**
     * 해당 수 만큼 가게 신고가 누적되면 실제로 가게가 삭제가 됩니다.
     */
    private static final int DELETE_REPORTS_COUNT = 3;

    private final StoreRepository storeRepository;
    private final StoreDeleteRequestRepository storeDeleteRequestRepository;

    @CacheEvict(cacheNames = USER_STORES_COUNTS, key = "#userId")
    @Transactional
    public StoreInfoResponse registerStore(RegisterStoreRequest request, Long userId) {
        Store store = storeRepository.save(request.toStore(userId));
        return StoreInfoResponse.of(store);
    }

    @Transactional
    public StoreInfoResponse updateStore(Long storeId, UpdateStoreRequest request) {
        Store store = StoreServiceUtils.findStoreByIdFetchJoinMenu(storeRepository, storeId);
        store.updateInfo(request.getStoreName(), request.getStoreType(), request.getLatitude(), request.getLongitude());
        store.updatePaymentMethods(request.getPaymentMethods());
        store.updateAppearanceDays(request.getAppearanceDays());
        store.updateMenu(request.toMenus(store));
        return StoreInfoResponse.of(store);
    }

    @Transactional
    public StoreDeleteResponse deleteStore(Long storeId, DeleteStoreRequest request, Long userId) {
        Store store = StoreServiceUtils.findStoreById(storeRepository, storeId);
        List<Long> reporters = storeDeleteRequestRepository.findAllUserIdByStoreIdWithLock(storeId);
        if (reporters.contains(userId)) {
            throw new ConflictException(String.format("사용자 (%s)는 가게 (%s)에 대해 이미 삭제 요청을 하였습니다", userId, storeId), CONFLICT_DELETE_REQUEST_STORE);
        }
        storeDeleteRequestRepository.save(request.toEntity(store, userId));
        return StoreDeleteResponse.of(deleteStoreIfSatisfyCondition(store, reporters));
    }

    private boolean deleteStoreIfSatisfyCondition(Store store, List<Long> reporters) {
        if (reporters.size() + 1 >= DELETE_REPORTS_COUNT) {
            store.delete();
            return true;
        }
        return false;
    }

}
