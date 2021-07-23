package com.depromeet.threedollar.admin.common.dto

import com.depromeet.threedollar.common.exception.ErrorCode

data class ApiResponse<T>(
    val resultCode: String,
    val message: String,
    val data: T?
) {

    companion object {
        val OK = success("OK")

        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse("", "", data)
        }

        fun error(errorCode: ErrorCode): ApiResponse<Nothing> {
            return ApiResponse(errorCode.code, errorCode.message, null)
        }

        fun error(errorCode: ErrorCode, message: String?): ApiResponse<Nothing> {
            return ApiResponse(errorCode.code, message ?: errorCode.message, null)
        }
    }

}
