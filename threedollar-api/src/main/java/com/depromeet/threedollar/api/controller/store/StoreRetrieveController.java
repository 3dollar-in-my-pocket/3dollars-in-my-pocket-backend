package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.GeoCoordinate;
import com.depromeet.threedollar.api.config.resolver.Coordinate;
import com.depromeet.threedollar.api.config.resolver.MapCoordinate;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.service.store.StoreRetrieveService;
import com.depromeet.threedollar.api.service.store.dto.request.*;
import com.depromeet.threedollar.api.service.store.dto.request.deprecated.RetrieveMyStoresV2Request;
import com.depromeet.threedollar.api.service.store.dto.request.deprecated.RetrieveStoreGroupByCategoryV2Request;
import com.depromeet.threedollar.api.service.store.dto.response.*;
import com.depromeet.threedollar.api.service.store.dto.response.deprecated.StoresGroupByDistanceV2Response;
import com.depromeet.threedollar.api.service.store.dto.response.deprecated.StoresGroupByReviewV2Response;
import com.depromeet.threedollar.api.service.store.dto.response.deprecated.StoresCursorV2Response;
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

    @ApiOperation(value = "메인 페이지 - 위도, 경도 기준 내 주위의 가게 목록을 조회합니다", notes = "orderType: 정렬이 필요한 경우 category: 카테고리 필터링이 필요한 경우")
    @GetMapping("/api/v2/stores/near")
    public ApiResponse<List<StoreWithVisitsAndDistanceResponse>> getNearStores(@Valid RetrieveNearStoresRequest request,
                                                                               @GeoCoordinate Coordinate geoCoordinate,
                                                                               @MapCoordinate Coordinate mapCoordinate) {
        return ApiResponse.success(storeRetrieveService.getNearStores(request, geoCoordinate, mapCoordinate));
    }

    @ApiOperation("가게 상세 페이지 - 특정 가게의 정보를 상세 조회합니다")
    @GetMapping("/api/v2/store")
    public ApiResponse<StoreDetailResponse> getDetailStoreInfo(@Valid RetrieveStoreDetailRequest request,
                                                               @GeoCoordinate Coordinate geoCoordinate) {
        return ApiResponse.success(storeRetrieveService.getDetailStoreInfo(request, geoCoordinate));
    }

    @ApiOperation("[인증] 마이페이지 - 내가 제보한 가게 목록들을 스크롤 페이지네이션으로 조회합니다 (삭제된 가게 포함 O)")
    @Auth
    @GetMapping("/api/v3/stores/me")
    public ApiResponse<StoresCursorResponse> retrieveMyReportedStoreHistories(@Valid RetrieveMyStoresRequest request, @UserId Long userId) {
        return ApiResponse.success(storeRetrieveService.retrieveMyReportedStoreHistories(request, userId));
    }

    /**
     * v2.1.1 부터 Deprecated
     * 내가 제보한 가게 조회시, 삭제된 가게들을 반환하되, 삭제된 가게라고 표기해줘야하는 이슈에 대응하기 위함. (호환성을 유지하기 위한 API)
     * use GET /api/v3/stores/me
     */
    @Deprecated
    @ApiOperation("[인증] 마이페이지 - 내가 제보한 가게 목록들을 스크롤 페이지네이션으로 조회합니다 (삭제된 가게 포함 X)")
    @Auth
    @GetMapping("/api/v2/stores/me")
    public ApiResponse<StoresCursorV2Response> retrieveMyReportedStoreHistoriesV2(@Valid RetrieveMyStoresV2Request request,
                                                                                  @GeoCoordinate(required = false) Coordinate geoCoordinate,
                                                                                  @UserId Long userId) {
        return ApiResponse.success(storeRetrieveService.retrieveMyReportedStoreHistoriesV2(request, geoCoordinate, userId));
    }

    /**
     * v2.1부터 Deprecated
     * GET /api/v2/stores/near로 통합 관리
     */
    @Deprecated
    @ApiOperation("[Deprecated] 가게 카테고리별 조회 페이지 - 거리순으로 특정 메뉴를 판매하는 가게 목록을 조회합니다")
    @GetMapping("/api/v2/stores/distance")
    public ApiResponse<StoresGroupByDistanceV2Response> getStoresGroupByDistance(@Valid RetrieveStoreGroupByCategoryV2Request request,
                                                                                 @GeoCoordinate Coordinate geoCoordinate,
                                                                                 @MapCoordinate Coordinate mapCoordinate) {
        return ApiResponse.success(storeRetrieveService.getNearStoresGroupByDistance(request, geoCoordinate, mapCoordinate));
    }

    /**
     * v2.1부터 Deprecated
     * GET /api/v2/stores/near로 통합 관리
     */
    @Deprecated
    @ApiOperation("[Deprecated] 가게 카테고리별 조회 페이지 - 리뷰순으로 특정 메뉴를 판매하는 가게 목록을 조회합니다")
    @GetMapping("/api/v2/stores/review")
    public ApiResponse<StoresGroupByReviewV2Response> getStoresGroupByReview(@Valid RetrieveStoreGroupByCategoryV2Request request,
                                                                             @GeoCoordinate Coordinate coordinate,
                                                                             @MapCoordinate Coordinate mapCoordinate) {
        return ApiResponse.success(storeRetrieveService.getNearStoresGroupByReview(request, coordinate, mapCoordinate));
    }

}
