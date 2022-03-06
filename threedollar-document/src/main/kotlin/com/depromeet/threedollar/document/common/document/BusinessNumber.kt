package com.depromeet.threedollar.document.common.document

import com.depromeet.threedollar.common.exception.model.InvalidException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import java.util.regex.Pattern

/**
 * 사업자 번호
 * 000-00-00000
 * TODO 사업자 번호 유효성 로직 추가
 */
private val BUSINESS_NUMBER_REGEX = Pattern.compile("(\\d{3})-(\\d{2})-(\\d{5})")
private const val SEPARATOR = "-"

data class BusinessNumber(
    private val first: String,
    private val second: String,
    private val third: String
) {

    fun getNumberWithSeparator(): String {
        return first + SEPARATOR + second + SEPARATOR + third
    }

    companion object {
        fun of(number: String): BusinessNumber {
            if (!BUSINESS_NUMBER_REGEX.matcher(number).matches()) {
                throw InvalidException("잘못된 사업자 번호 (${number}) 입니다. 사업자 번호 형식은 [000-00-00000] 입니다", ErrorCode.INVALID_BUSINESS_NUMBER_FORMAT)
            }
            val separatedNumber = number.split(SEPARATOR)
            return BusinessNumber(
                first = separatedNumber[0],
                second = separatedNumber[1],
                third = separatedNumber[2]
            )
        }
    }

}
