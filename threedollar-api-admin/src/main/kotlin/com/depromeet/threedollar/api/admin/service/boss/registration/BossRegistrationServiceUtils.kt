package com.depromeet.threedollar.api.admin.service.boss.registration

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossBossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.Registration

object BossRegistrationServiceUtils {

    fun findWaitingRegistrationById(bossRegistrationRepository: BossBossRegistrationRepository, registrationId: String): Registration {
        return bossRegistrationRepository.findWaitingRegistrationById(registrationId)
            ?: throw NotFoundException("해당하는 가입 신청 (${registrationId})은 존재하지 않습니다", ErrorCode.NOTFOUND_SIGNUP_REGISTRATION)
    }

}
