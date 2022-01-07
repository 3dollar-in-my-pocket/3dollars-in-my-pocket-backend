package com.depromeet.threedollar.consumer.controller

class ApiResponse<T>(
    val code: String,
    val message: String,
    val data: T?
) {
    companion object {
        val SUCCESS = success("OK")
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse<T>("", "", data)
        }
    }

}

