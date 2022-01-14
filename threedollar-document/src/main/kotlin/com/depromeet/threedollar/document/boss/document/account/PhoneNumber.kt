package com.depromeet.threedollar.document.boss.document.account

data class PhoneNumber(
    private val first: String,
    private val second: String,
    private val third: String
) {

    init {
        // TODO Validate
    }

    fun getNumber(): String {
        return first + SEPARATOR + second + SEPARATOR + third
    }

    companion object {
        private const val SEPARATOR = "-"
    }

}

