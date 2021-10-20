package com.depromeet.threedollar.api.controller.store;

import com.depromeet.threedollar.api.config.interceptor.Auth;
import com.depromeet.threedollar.api.config.resolver.UserId;
import com.depromeet.threedollar.api.service.store.StoreImageService;
import com.depromeet.threedollar.api.service.store.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.api.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.application.common.dto.ApiResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreImageController {

    private final StoreImageService storeImageService;

    @ApiOperation("특정 가게에 등록된 이미지들을 조회합니다.")
    @GetMapping("/api/v2/store/{storeId}/images")
    public ApiResponse<List<StoreImageResponse>> retrieveStoreImages(@PathVariable Long storeId) {
        return ApiResponse.success(storeImageService.retrieveStoreImages(storeId));
    }

    @ApiOperation("[인증] 가게의 이미지들을 등록합니다.")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @Auth
    @PostMapping("/api/v2/store/images")
    public ApiResponse<List<StoreImageResponse>> addStoreImage(@RequestPart(value = "images") List<MultipartFile> images,
                                                               @Valid AddStoreImageRequest request, @UserId Long userId) {
        return ApiResponse.success(storeImageService.addStoreImages(request, images, userId));
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
