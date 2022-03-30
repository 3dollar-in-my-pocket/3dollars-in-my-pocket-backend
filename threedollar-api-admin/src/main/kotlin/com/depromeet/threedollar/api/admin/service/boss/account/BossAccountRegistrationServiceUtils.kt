package com.depromeet.threedollar.api.admin.service.boss.account

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.Registration
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository

object BossAccountRegistrationServiceUtils {

    fun findWaitingRegistrationById(registrationRepository: RegistrationRepository, registrationId: String): Registration {
        return registrationRepository.findWaitingRegistrationById(registrationId)
            ?: throw NotFoundException("해당하는 가입 신청 (${registrationId})은 존재하지 않습니다", ErrorCode.NOTFOUND_SIGNUP_REGISTRATION)
    }

}
