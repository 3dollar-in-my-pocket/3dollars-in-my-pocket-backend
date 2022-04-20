package com.depromeet.threedollar.domain.mongo.common.domain

import com.depromeet.threedollar.common.exception.model.InvalidException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import java.util.regex.Pattern

/**
 * 사업자 번호
 * 000-00-00000
 */
private val BUSINESS_NUMBER_REGEX: Pattern = Pattern.compile("(\\d{3})-(\\d{2})-(\\d{5})")
private const val SEPARATOR: String = "-"
private val CHECKSUM_LOGIC_NUMBERS: List<Int> = listOf(1, 3, 7, 1, 3, 7, 1, 3, 5)

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
            if (!isValid(separatedNumber[0] + separatedNumber[1] + separatedNumber[2])) {
                throw InvalidException("잘못된 사업자 번호 (${number}) 입니다. 사업자 번호 체크섬이 깨집니다", ErrorCode.INVALID_BUSINESS_NUMBER_FORMAT)
            }
            return BusinessNumber(
                first = separatedNumber[0],
                second = separatedNumber[1],
                third = separatedNumber[2]
            )
        }

        private fun isValid(number: String): Boolean {
            var sum = 0
            for (i in CHECKSUM_LOGIC_NUMBERS.indices) {
                sum += number[i].digitToInt() * CHECKSUM_LOGIC_NUMBERS[i]
            }
            sum += number[8].digitToInt() * 5 / 10
            val checkNum = (10 - sum % 10) % 10
            return checkNum == number[9].digitToInt()
        }
    }

}
