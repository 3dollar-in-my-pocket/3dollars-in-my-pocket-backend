package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.service.store.StoreService;
import com.depromeet.threedollar.api.service.store.dto.request.RegisterStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.request.DeleteStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.request.UpdateStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.response.StoreDeleteResponse;
import com.depromeet.threedollar.api.service.store.dto.response.StoreInfoResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.common.exception.model.InternalServerException;
import com.depromeet.threedollar.domain.event.store.StoreCreatedEvent;
import com.depromeet.threedollar.domain.event.store.StoreDeletedEvent;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.depromeet.threedollar.common.exception.ErrorCode.INTERNAL_SERVER_UPDATE_STORE_OPTIMISTIC_LOCK_FAILED_EXCEPTION;

@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;
    private final ApplicationEventPublisher eventPublisher;

    @ApiOperation("[인증] 가게 등록 페이지 - 새로운 가게를 제보합니다")
    @Auth
    @PostMapping("/api/v2/store")
    public ApiResponse<StoreInfoResponse> registerStore(
        @Valid @RequestBody RegisterStoreRequest request,
        @UserId Long userId
    ) {
        StoreInfoResponse response = storeService.registerStore(request, userId);
        eventPublisher.publishEvent(StoreCreatedEvent.of(response.getStoreId(), userId));
        return ApiResponse.success(response);
    }

    @ApiOperation("[인증] 가게 상세 페이지 - 특정 가게의 정보를 수정합니다")
    @Auth
    @PutMapping("/api/v2/store/{storeId}")
    public ApiResponse<StoreInfoResponse> updateStore(
        @PathVariable Long storeId,
        @Valid @RequestBody UpdateStoreRequest request
    ) {
        try {
            return ApiResponse.success(storeService.updateStore(storeId, request));
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new InternalServerException(String.format("가게 (%s)를 수정하는 도중 잠금 충돌이 발생하였습니다. message: (%s)", storeId, e),
                INTERNAL_SERVER_UPDATE_STORE_OPTIMISTIC_LOCK_FAILED_EXCEPTION);
        }
    }

    @ApiOperation("[인증] 가게 상세 페이지 - 특정 가게의 정보를 삭제 요청합니다")
    @Auth
    @DeleteMapping("/api/v2/store/{storeId}")
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
