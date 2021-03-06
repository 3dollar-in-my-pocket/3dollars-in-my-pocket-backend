package com.depromeet.threedollar.api.userservice.controller.store;

import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse;
import com.depromeet.threedollar.api.userservice.config.interceptor.Auth;
import com.depromeet.threedollar.api.userservice.config.resolver.UserId;
import com.depromeet.threedollar.api.userservice.service.store.StoreImageRetrieveService;
import com.depromeet.threedollar.api.userservice.service.store.StoreImageService;
import com.depromeet.threedollar.api.userservice.service.store.dto.request.AddStoreImageRequest;
import com.depromeet.threedollar.api.userservice.service.store.dto.response.StoreImageResponse;
import com.depromeet.threedollar.common.exception.model.InvalidException;
import com.depromeet.threedollar.common.exception.type.ErrorCode;
import com.depromeet.threedollar.domain.rds.domain.userservice.store.StoreImage;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreImageController {

    private final StoreImageService storeImageService;
    private final StoreImageRetrieveService storeImageRetrieveService;


    @ApiOperation("[인증] 가게에 등록된 이미지 목록을 조회합니다")
    @Auth
    @GetMapping("/v2/store/{storeId}/images")
    public ApiResponse<List<StoreImageResponse>> getStoreImages(
        @PathVariable Long storeId
    ) {
        return ApiResponse.success(storeImageRetrieveService.getStoreImages(storeId));
    }

    @ApiOperation("[인증] 가게에 신규 이미지들을 등록합니다")
    @Auth
    @PostMapping("/v2/store/images")
    public ApiResponse<List<StoreImageResponse>> addStoreImage(
        @RequestPart List<MultipartFile> images,
        @Valid AddStoreImageRequest request,
        @UserId Long userId
    ) {
        if (images.isEmpty()) {
            throw new InvalidException(String.format("유저(%s)가 업로드 한 가게(%s) 이미지 업로드를 위한 파일이 비어있습니다", userId, request.getStoreId()), ErrorCode.E400_MISSING_FILE);
        }
        List<StoreImage> storeImages = storeImageService.uploadStoreImages(request, images, userId);
        return ApiResponse.success(storeImageService.addStoreImages(storeImages));
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
