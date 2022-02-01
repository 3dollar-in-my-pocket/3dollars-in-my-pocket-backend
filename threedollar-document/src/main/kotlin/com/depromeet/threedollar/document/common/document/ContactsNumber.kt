package com.depromeet.threedollar.document.common.document

data class ContactsNumber(
    private val first: String,
    private val second: String,
    private val third: String
) {

    fun getNumber(): String {
        return first + SEPARATOR + second + SEPARATOR + third
    }

    companion object {
        fun of(number: String): ContactsNumber {
            // TODO 연락처 Validation
            return ContactsNumber(
                first = number.split(SEPARATOR)[0],
                second = number.split(SEPARATOR)[1],
                third = number.split(SEPARATOR)[2]
            )
        }

        private const val SEPARATOR = "-"
    }

}

