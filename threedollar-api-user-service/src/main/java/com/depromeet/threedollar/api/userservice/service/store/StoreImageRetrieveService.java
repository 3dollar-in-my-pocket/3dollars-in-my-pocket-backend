package com.depromeet.threedollar.api.userservice.service.store;

import com.depromeet.threedollar.api.core.service.service.userservice.store.StoreServiceHelper;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImageRepository;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StoreImageRetrieveService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;

    @Transactional(readOnly = true)
    public List<StoreImageResponse> getStoreImages(Long storeId) {
        StoreServiceHelper.validateExistsStore(storeRepository, storeId);
        return storeImageRepository.findAllByStoreId(storeId).stream()
            .map(StoreImageResponse::of)
            .collect(Collectors.toList());
    }

}
