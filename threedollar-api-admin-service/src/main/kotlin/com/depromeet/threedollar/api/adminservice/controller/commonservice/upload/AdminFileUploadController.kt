package com.depromeet.threedollar.api.adminservice.controller.commonservice.upload

import com.depromeet.threedollar.api.adminservice.config.interceptor.Auth
import com.depromeet.threedollar.api.core.common.dto.response.ApiResponse
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.infrastructure.s3.common.type.FileType
import com.depromeet.threedollar.infrastructure.s3.provider.UploadProvider
import com.depromeet.threedollar.infrastructure.s3.provider.dto.request.ImageUploadFileRequest
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class AdminFileUploadController(
    private val uploadProvider: UploadProvider,
) {

    @ApiOperation("파일을 업로드합니다")
    @Auth
    @PostMapping("/v1/upload/{fileType}")
    fun uploadFile(
        @RequestPart file: MultipartFile,
        @PathVariable fileType: FileType,
    ): ApiResponse<String> {
        val imageUrl = uploadProvider.uploadFile(ImageUploadFileRequest.of(file, fileType, ApplicationType.ADMIN_API))
        return ApiResponse.success(imageUrl)
    }

    @ApiOperation("파일을 bulk로 업로드합니다")
    @Auth
    @PostMapping("/v1/upload/{fileType}/bulk")
    fun uploadFiles(
        @RequestPart files: List<MultipartFile>,
        @PathVariable fileType: FileType,
    ): ApiResponse<List<String>> {
        val uploadRequests = files.map { file -> ImageUploadFileRequest.of(file, fileType, ApplicationType.ADMIN_API) }

        val uploadResponses = uploadProvider.uploadFiles(uploadRequests).asSequence()
            .sortedBy { it.sequence }
            .map { it.fileUrl }
            .toList()

        return ApiResponse.success(uploadResponses)
    }

}
