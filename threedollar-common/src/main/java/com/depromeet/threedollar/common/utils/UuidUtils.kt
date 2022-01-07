package com.depromeet.threedollar.common.utils

import java.util.*

object UuidUtils {

    private const val VERSION = "v1"

    @JvmStatic
    fun generate(): String {
        return "${VERSION}-${UUID.randomUUID()}"
    }

}
