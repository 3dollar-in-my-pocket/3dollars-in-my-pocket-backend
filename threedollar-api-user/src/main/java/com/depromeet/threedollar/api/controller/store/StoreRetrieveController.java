package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.GeoCoordinate;
import com.depromeet.threedollar.common.model.CoordinateValue;
import com.depromeet.threedollar.api.config.resolver.MapCoordinate;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.service.store.StoreRetrieveService;
import com.depromeet.threedollar.api.service.store.dto.request.*;
import com.depromeet.threedollar.api.service.store.dto.response.*;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
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
    public ApiResponse<List<StoreWithVisitsAndDistanceResponse>> getNearStores(
        @Valid RetrieveNearStoresRequest request,
        @GeoCoordinate(required = false) CoordinateValue geoCoordinate,
        @MapCoordinate CoordinateValue mapCoordinate
    ) {
        return ApiResponse.success(storeRetrieveService.getNearStores(request, geoCoordinate, mapCoordinate));
    }

    @ApiOperation(value = "가게의 상세 정보를 조회합니다", notes = "startDate: 특정 날짜부터 방문한 인증 이력들을 조회")
    @GetMapping("/v2/store")
    public ApiResponse<StoreDetailResponse> getDetailStoreInfo(
        @Valid RetrieveStoreDetailRequest request,
        @GeoCoordinate CoordinateValue geoCoordinate
    ) {
        return ApiResponse.success(storeRetrieveService.getDetailStoreInfo(request, geoCoordinate));
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
        @MapCoordinate CoordinateValue mapCoordinate
    ) {
        return ApiResponse.success(storeRetrieveService.checkExistStoresNearby(request, mapCoordinate));
    }

}
