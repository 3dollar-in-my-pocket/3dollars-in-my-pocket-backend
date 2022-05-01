package com.depromeet.threedollar.api.admin.service.boss.registration

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistration

object BossRegistrationServiceUtils {

    fun findWaitingRegistrationById(bossRegistrationRepository: BossRegistrationRepository, registrationId: String): BossRegistration {
        return bossRegistrationRepository.findWaitingRegistrationById(registrationId)
            ?: throw NotFoundException("해당하는 가입 신청 (${registrationId})은 존재하지 않습니다", ErrorCode.NOTFOUND_SIGNUP_REGISTRATION)
    }

}
