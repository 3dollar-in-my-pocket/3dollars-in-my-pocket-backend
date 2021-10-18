package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.service.store.GetDistanceToStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.response.*;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import com.depromeet.threedollar.api.service.store.StoreRetrieveService;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveNearStoresRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveMyStoresRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveStoreGroupByCategoryRequest;
import com.depromeet.threedollar.api.service.store.dto.request.RetrieveStoreDetailInfoRequest;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreRetrieveController {

    private final StoreRetrieveService storeRetrieveService;

    @ApiOperation("위도, 경도 주위 가게 목록을 조회합니다.")
    @GetMapping("/api/v2/stores/near")
    public ApiResponse<List<StoreInfoResponse>> getNearStores(@Valid RetrieveNearStoresRequest request) {
        return ApiResponse.success(storeRetrieveService.getNearStores(request));
    }

    @ApiOperation("특정 가게의 정보를 조회합니다.")
    @GetMapping("/api/v2/store")
    public ApiResponse<StoreDetailResponse> getDetailStoreInfo(@Valid RetrieveStoreDetailInfoRequest request) {
        return ApiResponse.success(storeRetrieveService.getDetailStoreInfo(request));
    }

    @ApiOperation("특정 가게에 등록된 이미지들을 조회합니다.")
    @GetMapping("/api/v2/store/{storeId}/images")
    public ApiResponse<List<StoreImageResponse>> retriveStoreImages(@PathVariable Long storeId) {
        return ApiResponse.success(storeRetrieveService.retrieveStoreImages(storeId));
    }

    @ApiOperation("[인증] 사용자가 작성한 가게의 정보를 조회합니다. (페이지네이션)")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @GetMapping("/api/v2/stores/me")
    public ApiResponse<StoresScrollResponse> getMyStores(@Valid RetrieveMyStoresRequest request, @UserId Long userId) {
        return ApiResponse.success(storeRetrieveService.retrieveMyStores(request, userId));
    }

    @ApiOperation("거리순으로 특정 카테고리의 가게 정보를 조회합니다.")
    @GetMapping("/api/v2/stores/distance")
    public ApiResponse<StoresGroupByDistanceResponse> getStoresGroupByDistance(@Valid RetrieveStoreGroupByCategoryRequest request) {
        return ApiResponse.success(storeRetrieveService.retrieveStoresGroupByDistance(request));
    }

    @ApiOperation("리뷰순으로 특정 카테고리의 가게 정보를 조회합니다.")
    @GetMapping("/api/v2/stores/review")
    public ApiResponse<StoresGroupByReviewResponse> getStoresGroupByReview(@Valid RetrieveStoreGroupByCategoryRequest request) {
        return ApiResponse.success(storeRetrieveService.retrieveStoresGroupByRating(request));
    }

    @ApiOperation("가게와의 거리를 계산합니다.")
    @GetMapping("/api/v2/store/distance/{storeId}")
    public ApiResponse<StoreDistanceResponse> getDistanceToStore(@PathVariable Long storeId, @Valid GetDistanceToStoreRequest request) {
        return ApiResponse.success(storeRetrieveService.getDistanceToStore(storeId, request));
    }

}
