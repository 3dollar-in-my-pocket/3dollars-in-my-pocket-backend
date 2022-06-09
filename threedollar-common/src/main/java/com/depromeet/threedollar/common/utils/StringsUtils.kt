package com.depromeet.threedollar.common.utils

object StringsUtils {

    @JvmStatic
    fun defaultIfNull(value: String?, defaultValue: String): String {
        return value ?: defaultValue
    }

    @JvmStatic
    fun defaultIfBlank(value: String?, defaultValue: String): String {
        if (value.isNullOrBlank()) {
            return defaultValue
        }
        return value
    }

}
