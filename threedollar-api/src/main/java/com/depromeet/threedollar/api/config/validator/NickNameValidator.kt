package com.depromeet.threedollar.api.config.validator

import org.springframework.stereotype.Component
import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

@Component
class NickNameValidator : ConstraintValidator<NickName, String> {

    override fun isValid(nickName: String?, context: ConstraintValidatorContext): Boolean {
        if (nickName == null) {
            return false
        }
        return NICKNAME_REGEX.matcher(nickName).matches()
    }

    /**
     * 닉네임 규칙
     * - [한글, 영대소문자, 숫자]로 시작 및 종료
     * - 중간에는 [한글, 영대소문자, 숫자, '-', '_', 공백] 가능.
     * - 2자 이상 10자 이하.
     */
    companion object {
        private val NICKNAME_REGEX = Pattern.compile("^[ㄱ-ㅎ가-힣a-zA-Z0-9][ㄱ-ㅎ가-힣\\sa-zA-Z0-9_-]{0,8}[ㄱ-ㅎ가-힣a-zA-Z0-9]$")
    }

}
