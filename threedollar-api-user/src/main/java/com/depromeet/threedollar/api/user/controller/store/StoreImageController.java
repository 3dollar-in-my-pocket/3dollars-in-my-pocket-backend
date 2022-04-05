package com.depromeet.threedollar.api.user.controller.store;

import com.depromeet.threedollar.api.user.config.interceptor.Auth;
import com.depromeet.threedollar.api.user.config.resolver.UserId;
import com.depromeet.threedollar.api.user.service.store.StoreImageService;
import com.depromeet.threedollar.api.user.service.store.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.api.user.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.api.core.common.dto.ApiResponse;
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

    @ApiOperation("가게에 등록된 이미지 목록을 조회합니다")
    @GetMapping("/v2/store/{storeId}/images")
    public ApiResponse<List<StoreImageResponse>> getStoreImages(
        @PathVariable Long storeId
    ) {
        return ApiResponse.success(storeImageService.getStoreImages(storeId));
    }

    @ApiOperation("[인증] 가게에 신규 이미지들을 등록합니다")
    @Auth
    @PostMapping("/v2/store/images")
    public ApiResponse<List<StoreImageResponse>> addStoreImage(
        @RequestPart List<MultipartFile> images,
        @Valid AddStoreImageRequest request,
        @UserId Long userId
    ) {
        return ApiResponse.success(storeImageService.addStoreImages(request, images, userId));
    }

    @ApiOperation("[인증] 가게에 등록된 특정 이미지를 삭제합니다")
    @Auth
    @DeleteMapping("/v2/store/image/{imageId}")
    public ApiResponse<String> deleteStoreImage(
        @PathVariable Long imageId
    ) {
        storeImageService.deleteStoreImage(imageId);
        return ApiResponse.OK;
    }

}
