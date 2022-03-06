package com.depromeet.threedollar.admin.service.boss.registration

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.document.boss.document.registration.Registration
import com.depromeet.threedollar.document.boss.document.registration.RegistrationRepository

object BossRegistrationServiceUtils {

    fun findWaitingRegistrationById(registrationRepository: RegistrationRepository, registrationId: String): Registration {
        return registrationRepository.findWaitingRegistrationById(registrationId)
            ?: throw NotFoundException("해당하는 가입 신청 (${registrationId})은 존재하지 않습니다", ErrorCode.NOTFOUND_SIGNUP_REGISTRATION)
    }

}
