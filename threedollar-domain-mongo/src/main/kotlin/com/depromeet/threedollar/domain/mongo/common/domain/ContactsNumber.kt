package com.depromeet.threedollar.domain.mongo.common.domain

import com.depromeet.threedollar.common.exception.model.InvalidException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import java.util.regex.Pattern

/**
 * 연락처 번호 (핸드폰 및 일반 전화 번호)
 */

private const val SEPARATOR = "-"
private val CONTACTS_NUMBER_REGEX = Pattern.compile("^\\d{2,3}-\\d{3,4}-\\d{4}\$")

data class ContactsNumber(
    private val first: String,
    private val second: String,
    private val third: String
) {

    fun getNumberWithSeparator(): String {
        return first + SEPARATOR + second + SEPARATOR + third
    }

    companion object {
        fun of(number: String): ContactsNumber {
            if (!CONTACTS_NUMBER_REGEX.matcher(number).matches()) {
                throw InvalidException("잘못된 전화번호 (${number}) 형식 입니다. 올바른 연락처 형식은 01X-0000-0000 입니다.", ErrorCode.INVALID_CONTACTS_NUMBER_FORMAT)
            }
            return ContactsNumber(
                first = number.split(SEPARATOR)[0],
                second = number.split(SEPARATOR)[1],
                third = number.split(SEPARATOR)[2]
            )
        }
    }

}

