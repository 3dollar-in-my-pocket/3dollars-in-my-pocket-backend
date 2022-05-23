package com.depromeet.threedollar.common.utils

private const val TRACE_ID_PREFIX = "TraceId"

/**
 * 로그 추적에 사용되는 TraceId (RequestId)
 */
object TraceIdUtils {

    fun generate(): String {
        return "${TRACE_ID_PREFIX}-${UuidUtils.generate()}"
    }

}
