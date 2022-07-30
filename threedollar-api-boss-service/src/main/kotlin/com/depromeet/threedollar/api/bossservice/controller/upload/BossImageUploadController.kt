package com.depromeet.threedollar.api.bossservice.controller.upload

import com.depromeet.threedollar.api.bossservice.controller.upload.dto.response.BossImageUploadResponse
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.infrastructure.s3.common.type.FileType
import com.depromeet.threedollar.infrastructure.s3.provider.UploadProvider
import com.depromeet.threedollar.infrastructure.s3.provider.dto.request.ImageUploadFileRequest
import com.depromeet.threedollar.infrastructure.s3.provider.dto.response.UploadFileWithSequenceResponse
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

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

    @ApiOperation("파일 목록을 bulk 업로드하고 스토리지의 URL 목록을 받아옵니다. (요청한 파일 순서대로 응답됩니다)", notes = "파일을 스토리지에 저장하고 URI만 받아옵니다 (차후 해당 URI을 API 호출할때 URL로 전송)")
    @PostMapping("/v1/upload/{fileType}/bulk")
    fun uploadFiles(
        @RequestPart files: List<MultipartFile>,
        @PathVariable fileType: FileType,
    ): ApiResponse<List<BossImageUploadResponse>> {
        val uploadFileRequests = files.map { file -> ImageUploadFileRequest.of(file, fileType, ApplicationType.BOSS_API) }
        val uploadFiles: List<UploadFileWithSequenceResponse> = uploadProvider.uploadFiles(uploadFileRequests)
        val uploadResponses = uploadFiles.asSequence()
            .sortedBy { response -> response.sequence }
            .map { response -> BossImageUploadResponse.of(response.fileUrl) }
            .toList()
        return ApiResponse.success(uploadResponses)
    }

}
