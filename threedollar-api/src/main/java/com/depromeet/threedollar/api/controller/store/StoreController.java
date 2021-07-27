package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.common.dto.ApiResponse;
import com.depromeet.threedollar.api.service.store.StoreImageService;
import com.depromeet.threedollar.api.service.store.StoreService;
import com.depromeet.threedollar.api.service.store.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.api.service.store.dto.request.DeleteStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.request.UpdateStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.request.AddStoreRequest;
import com.depromeet.threedollar.api.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.api.service.store.dto.response.StoreInfoResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;
    private final StoreImageService storeImageService;

    @ApiOperation("[인증] 새로운 가게 정보를 등록합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @PostMapping("/api/v2/store")
    public ApiResponse<StoreInfoResponse> addStore(@Valid @RequestBody AddStoreRequest request,
                                                   @UserId Long userId) {
        return ApiResponse.success(storeService.addStore(request, userId));
    }

    @ApiOperation("[인증] 특정 가게의 정보를 수정합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @PutMapping("/api/v2/store/{storeId}")
    public ApiResponse<StoreInfoResponse> updateStore(@PathVariable Long storeId,
                                                      @Valid @RequestBody UpdateStoreRequest request,
                                                      @UserId Long userId) {
        return ApiResponse.success(storeService.updateStore(storeId, request, userId));
    }

    @ApiOperation("[인증] 특정 가게의 정보를 삭제 요청합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @DeleteMapping("/api/v2/store/{storeId}")
    public ApiResponse<String> deleteStore(@Valid DeleteStoreRequest request,
                                           @PathVariable Long storeId,
                                           @UserId Long userId) {
        storeService.deleteStore(storeId, request, userId);
        return ApiResponse.SUCCESS;
    }

    @ApiOperation("[인증] 가게의 이미지를 등록합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @PostMapping("/api/v2/store/image")
    public ApiResponse<StoreImageResponse> addStoreImage(@Valid AddStoreImageRequest request,
                                                         @RequestPart(value = "image") MultipartFile multipartFile,
                                                         @UserId Long userId) {
        return ApiResponse.success(storeImageService.addStoreImage(request, multipartFile, userId));
    }

    @ApiOperation("[인증] 가게의 이미지를 삭제합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @DeleteMapping("/api/v2/store/image/{imageId}")
    public ApiResponse<String> deleteStoreImage(@PathVariable Long imageId) {
        storeImageService.deleteStoreImage(imageId);
        return ApiResponse.SUCCESS;
    }

}
