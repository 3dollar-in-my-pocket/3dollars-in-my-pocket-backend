package com.depromeet.threedollar.admin.controller.upload

import com.depromeet.threedollar.admin.config.interceptor.Auth
import com.depromeet.threedollar.application.common.dto.ApiResponse
import com.depromeet.threedollar.application.provider.upload.UploadProvider
import com.depromeet.threedollar.application.provider.upload.dto.request.ImageUploadFileRequest
import com.depromeet.threedollar.common.type.ApplicationType
import com.depromeet.threedollar.common.type.FileType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class AdminFileUploadController(
    private val uploadProvider: UploadProvider
) {

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
