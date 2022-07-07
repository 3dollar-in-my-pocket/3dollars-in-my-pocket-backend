package com.depromeet.threedollar.api.bossservice.service.auth

import com.depromeet.threedollar.api.bossservice.service.account.BossAccountServiceHelper
import com.depromeet.threedollar.api.bossservice.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.api.core.service.bossservice.category.BossStoreCategoryServiceHelper
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.NewBossAppliedRegistrationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class SignupService(
    private val bossAccountRepository: BossAccountRepository,
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun signUp(request: SignupRequest, socialId: String): String {
        BossAccountServiceHelper.validateNotExistsBossAccount(bossAccountRepository, socialId, request.socialType)
        validateDuplicateRegistration(socialId, request.socialType)
        BossStoreCategoryServiceHelper.validateExistsCategories(bossStoreCategoryRepository, request.storeCategoriesIds)

        val registration = request.toEntity(socialId)
        bossRegistrationRepository.save(registration)

        eventPublisher.publishEvent(NewBossAppliedRegistrationEvent.of(registration))
        return registration.id
    }

    private fun validateDuplicateRegistration(socialId: String, socialType: BossAccountSocialType) {
        if (bossRegistrationRepository.existsWaitingRegistrationBySocialIdAndSocialType(socialId, socialType)) {
            throw ForbiddenException("가입 승인 대기중인 사장님 게정(${socialId} - (${socialType}) 입니다.", ErrorCode.FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT)
        }
    }

}
