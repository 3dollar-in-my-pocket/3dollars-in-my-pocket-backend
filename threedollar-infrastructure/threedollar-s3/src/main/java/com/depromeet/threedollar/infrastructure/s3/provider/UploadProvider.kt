package com.depromeet.threedollar.infrastructure.s3.provider

import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors
import org.springframework.stereotype.Component
import com.depromeet.threedollar.infrastructure.s3.infra.FileStorageClient
import com.depromeet.threedollar.infrastructure.s3.provider.dto.request.UploadFileRequest
import com.depromeet.threedollar.infrastructure.s3.provider.dto.response.UploadFileWithSequenceResponse

@Component
class UploadProvider(
    private val fileStorageClient: FileStorageClient,
) {

    fun uploadFile(request: UploadFileRequest): String {
        request.validateAvailableUploadFile()
        val fileName = request.getFileNameWithBucketDirectory(request.file.originalFilename)
        fileStorageClient.uploadFile(request.file, fileName)
        return fileStorageClient.getFileUrl(fileName)
    }

    fun uploadFiles(requests: List<UploadFileRequest>): List<UploadFileWithSequenceResponse> {
        val uploadFutures = requests.mapIndexed { index, request ->
            CompletableFuture.supplyAsync {
                request.validateAvailableUploadFile()
                val fileName = request.getFileNameWithBucketDirectory(request.file.originalFilename)
                fileStorageClient.uploadFile(request.file, fileName)
                UploadFileWithSequenceResponse(
                    sequence = index,
                    fileUrl = fileStorageClient.getFileUrl(fileName)
                )
            }
        }

        return CompletableFuture.allOf(*uploadFutures.toTypedArray<CompletableFuture<*>>())
            .thenApply {
                uploadFutures.stream()
                    .map { uploadFuture -> uploadFuture.join() }
                    .collect(Collectors.toList())
            }
            .join()
    }

}
