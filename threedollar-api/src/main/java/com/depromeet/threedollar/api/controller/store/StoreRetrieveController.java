package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreRetrieveController {

    private final StoreRetrieveService storeRetrieveService;

    @ApiOperation("메인 페이지 - 위도, 경도 기준 내 주위의 가게 목록을 조회합니다.")
    @GetMapping("/api/v2/stores/near")
    public ApiResponse<List<StoreInfoResponse>> getNearStores(@Valid RetrieveNearStoresRequest request) {
        return ApiResponse.success(storeRetrieveService.getNearStores(request));
    }

    @ApiOperation("가게 상세 페이지 - 특정 가게의 정보를 상세 조회합니다.")
    @GetMapping("/api/v2/store")
    public ApiResponse<StoreDetailResponse> getDetailStoreInfo(@Valid RetrieveStoreDetailInfoRequest request) {
        return ApiResponse.success(storeRetrieveService.getDetailStoreInfo(request));
    }

    @ApiOperation("[인증] 마이페이지 - 내가 제보한 가게 목록들을 스크롤 페이지네이션으로 조회합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @GetMapping("/api/v2/stores/me")
    public ApiResponse<StoresScrollResponse> getMyStores(@Valid RetrieveMyStoresRequest request, @UserId Long userId) {
        return ApiResponse.success(storeRetrieveService.retrieveMyStores(request, userId));
    }

    @ApiOperation("가게 카테고리별 조회 페이지 - 거리순으로 특정 메뉴를 판매하는 가게 목록을 조회합니다.")
    @GetMapping("/api/v2/stores/distance")
    public ApiResponse<StoresGroupByDistanceResponse> getStoresGroupByDistance(@Valid RetrieveStoreGroupByCategoryRequest request) {
        return ApiResponse.success(storeRetrieveService.retrieveStoresGroupByDistance(request));
    }

    @ApiOperation("가게 카테고리별 조회 페이지 - 리뷰순으로 특정 메뉴를 판매하는 가게 목록을 조회합니다.")
    @GetMapping("/api/v2/stores/review")
    public ApiResponse<StoresGroupByReviewResponse> getStoresGroupByReview(@Valid RetrieveStoreGroupByCategoryRequest request) {
        return ApiResponse.success(storeRetrieveService.retrieveStoresGroupByRating(request));
    }

}
