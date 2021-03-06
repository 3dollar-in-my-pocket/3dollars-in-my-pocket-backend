package com.depromeet.threedollar.api.userservice.controller.store;

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse;
import com.depromeet.threedollar.api.core.service.config.resolver.DeviceLocation;
import com.depromeet.threedollar.api.core.service.config.resolver.MapLocation;
import com.depromeet.threedollar.api.userservice.config.interceptor.Auth;
import com.depromeet.threedollar.api.userservice.config.resolver.UserId;
import com.depromeet.threedollar.api.userservice.service.store.StoreRetrieveService;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.CheckExistsStoresNearbyRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.RetrieveAroundStoresRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.RetrieveMyStoresRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.RetrieveStoreDetailRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.CheckExistStoresNearbyResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreDetailResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreWithVisitsAndDistanceResponse;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoresCursorResponse;
import com.depromeet.threedollar.common.model.LocationValue;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreRetrieveController {

    private final StoreRetrieveService storeRetrieveService;

    @ApiOperation(value = "주위의 가게 목록을 조회합니다", notes = "orderType: 정렬이 필요한 경우 category: 카테고리 필터링이 필요한 경우")
    @GetMapping("/v2/stores/near")
    public ApiResponse<List<StoreWithVisitsAndDistanceResponse>> retrieveAroundStores(
        @Valid RetrieveAroundStoresRequest request,
        @DeviceLocation(required = false) LocationValue deviceLocation,
        @MapLocation LocationValue mapLocation
    ) {
        return ApiResponse.success(storeRetrieveService.retrieveAroundStores(request, deviceLocation, mapLocation));
    }

    @ApiOperation(value = "[인증] 가게의 상세 정보를 조회합니다", notes = "startDate: 특정 날짜부터 방문한 인증 이력들을 조회")
    @Auth
    @GetMapping("/v2/store")
    public ApiResponse<StoreDetailResponse> retrieveStoreDetailInfo(
        @Valid RetrieveStoreDetailRequest request,
        @DeviceLocation LocationValue deviceLocation
    ) {
        return ApiResponse.success(storeRetrieveService.retrieveStoreDetailInfo(request, deviceLocation));
    }

    @ApiOperation("[인증] 내가 제보한 가게 목록들을 조회합니다 (스크롤 페이지네이션)")
    @Auth
    @GetMapping("/v3/stores/me")
    public ApiResponse<StoresCursorResponse> retrieveMyReportedStoreHistories(
        @Valid RetrieveMyStoresRequest request,
        @UserId Long userId
    ) {
        return ApiResponse.success(storeRetrieveService.retrieveMyReportedStoreHistories(request, userId));
    }

    @ApiOperation("주변에 가게가 존재하는지 확인합니다")
    @GetMapping("/v1/stores/near/exists")
    public ApiResponse<CheckExistStoresNearbyResponse> checkExistStoresNearby(
        @Valid CheckExistsStoresNearbyRequest request,
        @MapLocation LocationValue mapLocation
    ) {
        return ApiResponse.success(storeRetrieveService.checkExistStoresNearby(request, mapLocation));
    }

}
