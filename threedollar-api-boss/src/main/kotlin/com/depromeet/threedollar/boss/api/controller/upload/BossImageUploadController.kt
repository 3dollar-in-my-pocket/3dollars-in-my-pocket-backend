package com.depromeet.threedollar.boss.api.controller.upload

import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.application.provider.upload.UploadProvider
import com.depromeet.threedollar.application.provider.upload.dto.request.ImageUploadFileRequest
import com.depromeet.threedollar.boss.api.controller.upload.dto.response.BossImageUploadResponse
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.type.FileType
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class BossImageUploadController(
    private val uploadProvider: UploadProvider
) {

    @ApiOperation("파일을 업로드하고 스토리지의 URL을 받아옵니다.")
    @PostMapping("/boss/v1/upload")
    fun uploadFile(
        @RequestPart(value = "file") file: MultipartFile,
        @RequestParam fileType: FileType
    ): ApiResponse<BossImageUploadResponse> {
        fileType.validateAvailableUploadInModule(ApplicationType.BOSS_API)
        val imageUrl = uploadProvider.uploadFile(ImageUploadFileRequest.of(fileType), file)
        return ApiResponse.success(BossImageUploadResponse(imageUrl))
    }

}
