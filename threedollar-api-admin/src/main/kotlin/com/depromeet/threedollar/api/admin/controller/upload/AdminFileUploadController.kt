package com.depromeet.threedollar.api.admin.controller.upload

import com.depromeet.threedollar.api.admin.config.interceptor.Auth
import com.depromeet.threedollar.api.core.common.dto.ApiResponse
import com.depromeet.threedollar.api.core.provider.upload.UploadProvider
import com.depromeet.threedollar.api.core.provider.upload.dto.request.ImageUploadFileRequest
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.type.FileType
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class AdminFileUploadController(
    private val uploadProvider: UploadProvider
) {

    @ApiOperation("파일을 업로드합니다")
    @Auth
    @PostMapping("/v1/upload/{fileType}")
    fun uploadFile(
        @RequestPart file: MultipartFile,
        @PathVariable fileType: FileType
    ): ApiResponse<String> {
        val imageUrl = uploadProvider.uploadFile(ImageUploadFileRequest.of(file, fileType, ApplicationType.ADMIN_API))
        return ApiResponse.success(imageUrl)
    }

}