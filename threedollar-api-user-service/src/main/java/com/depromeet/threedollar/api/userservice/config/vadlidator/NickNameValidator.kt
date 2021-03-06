package com.depromeet.threedollar.api.userservice.config.vadlidator

import org.springframework.stereotype.Component
import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * 닉네임 규칙
 * - [한글, 영대소문자, 숫자]로 시작 및 종료
 * - 중간에는 [한글, 영대소문자, 숫자, '-', '_', 공백] 가능.
 * - 2자 이상 10자 이하.
 */
private val NICKNAME_REGEX = Pattern.compile("^[ㄱ-ㅎ가-힣a-zA-Z\\d][ㄱ-ㅎ가-힣\\sa-zA-Z\\d_-]{0,8}[ㄱ-ㅎ가-힣a-zA-Z\\d]$")

@Component
class NickNameValidator : ConstraintValidator<NickName, String> {

    override fun isValid(nickName: String?, context: ConstraintValidatorContext): Boolean {
        if (nickName.isNullOrBlank()) {
            return false
        }
        return NICKNAME_REGEX.matcher(nickName).matches()
    }

}
