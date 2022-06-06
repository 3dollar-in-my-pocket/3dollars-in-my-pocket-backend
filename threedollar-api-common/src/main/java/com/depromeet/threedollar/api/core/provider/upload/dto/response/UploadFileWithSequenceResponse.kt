package com.depromeet.threedollar.api.core.provider.upload.dto.response

data class UploadFileWithSequenceResponse(
    val sequence: Int,
    val fileUrl: String,
) {

    companion object {
        @JvmStatic
        fun of(sequence: Int, fileUrl: String): UploadFileWithSequenceResponse {
            return UploadFileWithSequenceResponse(
                sequence = sequence,
                fileUrl = fileUrl
            )
        }
    }

}
