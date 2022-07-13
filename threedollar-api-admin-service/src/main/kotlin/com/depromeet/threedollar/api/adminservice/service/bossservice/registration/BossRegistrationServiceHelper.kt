package com.depromeet.threedollar.api.adminservice.service.bossservice.registration

import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistration
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository

object BossRegistrationServiceHelper {

    fun findWaitingRegistrationById(bossRegistrationRepository: BossRegistrationRepository, registrationId: String): BossRegistration {
        return bossRegistrationRepository.findWaitingRegistrationById(registrationId)
            ?: throw NotFoundException("해당하는 가입 신청 (${registrationId})은 존재하지 않습니다", ErrorCode.E404_NOT_EXISTS_SIGNUP_REGISTRATION)
    }

}
