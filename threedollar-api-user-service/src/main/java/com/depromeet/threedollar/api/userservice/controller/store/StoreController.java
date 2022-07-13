package com.depromeet.threedollar.api.userservice.controller.store;

import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
import com.depromeet.threedollar.api.userservice.config.interceptor.Auth;
import com.depromeet.threedollar.api.userservice.config.resolver.UserId;
import com.depromeet.threedollar.api.userservice.service.store.StoreService;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.DeleteStoreRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.RegisterStoreRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.UpdateStoreRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreDeleteResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.domain.rds.event.userservice.store.StoreCreatedEvent;
import com.depromeet.threedollar.domain.rds.event.userservice.store.StoreDeletedEvent;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.depromeet.threedollar.common.exception.type.ErrorCode.E500_INTERNAL_SERVER_UPDATE_STORE_CONCURRENCY;

@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;
    private final ApplicationEventPublisher eventPublisher;

    @ApiOperation("[인증] 새로운 가게를 제보합니다")
    @Auth
    @PostMapping("/v2/store")
    public ApiResponse<StoreInfoResponse> registerStore(
        @Valid @RequestBody RegisterStoreRequest request,
        @UserId Long userId
    ) {
        StoreInfoResponse response = storeService.registerStore(request, userId);
        eventPublisher.publishEvent(StoreCreatedEvent.of(response.getStoreId(), userId));
        return ApiResponse.success(response);
    }

    @ApiOperation("[인증] 특정 가게의 정보를 수정합니다")
    @Auth
    @PutMapping("/v2/store/{storeId}")
    public ApiResponse<StoreInfoResponse> updateStore(
        @PathVariable Long storeId,
        @Valid @RequestBody UpdateStoreRequest request
    ) {
        try {
            return ApiResponse.success(storeService.updateStore(storeId, request));
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new InternalServerException(String.format("가게 (%s)를 수정하는 도중 잠금 충돌이 발생하였습니다. message: (%s)", storeId, e), E500_INTERNAL_SERVER_UPDATE_STORE_CONCURRENCY);
        }
    }

    @ApiOperation("[인증] 특정 가게의 정보를 삭제 요청합니다")
    @Auth
    @DeleteMapping("/v2/store/{storeId}")
    public ApiResponse<StoreDeleteResponse> deleteStore(
        @Valid DeleteStoreRequest request,
        @PathVariable Long storeId,
        @UserId Long userId
    ) {
        StoreDeleteResponse response = storeService.deleteStore(storeId, request, userId);
        eventPublisher.publishEvent(StoreDeletedEvent.of(storeId, userId));
        return ApiResponse.success(response);
    }

}
