package com.depromeet.threedollar.api.service.store;

import com.depromeet.threedollar.api.service.store.dto.request.AddStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.request.DeleteStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.request.UpdateStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.response.StoreDeleteResponse;
import com.depromeet.threedollar.api.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.common.exception.model.ConflictException;
import com.depromeet.threedollar.domain.domain.store.Store;
import com.depromeet.threedollar.domain.domain.store.StoreRepository;
import com.depromeet.threedollar.domain.domain.storedelete.StoreDeleteRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StoreService {

    /**
     * MAX_DELETE_REQUEST 만큼 가게 신고가 누적되면 실제로 가게가 삭제가 됩니다.
     */
    private static final int MAX_DELETE_REQUEST = 3;

    private final StoreRepository storeRepository;
    private final StoreDeleteRequestRepository storeDeleteRequestRepository;

    @Transactional
    public StoreInfoResponse addStore(AddStoreRequest request, Long userId) {
        Store store = storeRepository.save(request.toStore(userId));
        return StoreInfoResponse.ofWithOutVisitsCount(store);
    }

    @Transactional
    public StoreInfoResponse updateStore(Long storeId, UpdateStoreRequest request, Long userId) {
        Store store = StoreServiceUtils.findStoreByIdFetchJoinMenu(storeRepository, storeId);
        store.updateLocation(request.getLatitude(), request.getLongitude());
        store.updateInfo(request.getStoreName(), request.getStoreType());
        store.updatePaymentMethods(request.getPaymentMethods());
        store.updateAppearanceDays(request.getAppearanceDays());
        store.updateMenu(request.toMenus(store));
        return StoreInfoResponse.ofWithOutVisitsCount(store);
    }

    @Transactional
    public StoreDeleteResponse deleteStore(Long storeId, DeleteStoreRequest request, Long userId) {
        Store store = StoreServiceUtils.findStoreById(storeRepository, storeId);

        List<Long> userIds = storeDeleteRequestRepository.findAllUserIdByStoreIdWithLock(storeId);
        if (userIds.contains(userId)) {
            throw new ConflictException(String.format("사용자 (%s)는 가게 (%s)에 대해 이미 삭제 요청을 하였습니다", userId, storeId));
        }
        storeDeleteRequestRepository.save(request.toEntity(storeId, userId));
        return StoreDeleteResponse.of(deleteStoreIfExceedLimit(store, userIds));
    }

    private boolean deleteStoreIfExceedLimit(Store store, List<Long> userIds) {
        if (userIds.size() + 1 >= MAX_DELETE_REQUEST) {
            store.delete();
            return true;
        }
        return false;
    }

}
