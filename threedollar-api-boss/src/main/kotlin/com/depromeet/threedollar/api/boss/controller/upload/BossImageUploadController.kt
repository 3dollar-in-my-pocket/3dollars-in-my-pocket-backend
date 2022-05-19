package com.depromeet.threedollar.api.boss.controller.upload

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import com.depromeet.threedollar.api.boss.controller.upload.dto.response.BossImageUploadResponse
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.provider.upload.UploadProvider
import com.depromeet.threedollar.api.core.provider.upload.dto.request.ImageUploadFileRequest
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.type.FileType
import io.swagger.annotations.ApiOperation

@RestController
class BossImageUploadController(
    private val uploadProvider: UploadProvider,
) {

    @ApiOperation("파일을 업로드하고 스토리지의 URL을 받아옵니다.", notes = "파일을 스토리지에 저장하고 URI만 받아옵니다 (차후 해당 URI을 API 호출할때 URL로 전송)")
    @PostMapping("/v1/upload/{fileType}")
    fun uploadFile(
        @RequestPart file: MultipartFile,
        @PathVariable fileType: FileType,
    ): ApiResponse<BossImageUploadResponse> {
        val imageUrl = uploadProvider.uploadFile(ImageUploadFileRequest.of(file, fileType, ApplicationType.BOSS_API))
        return ApiResponse.success(BossImageUploadResponse.of(imageUrl))
    }

}
