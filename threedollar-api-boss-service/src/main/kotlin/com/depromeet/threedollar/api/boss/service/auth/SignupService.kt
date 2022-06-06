package com.depromeet.threedollar.api.boss.service.auth

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.boss.service.account.BossAccountServiceUtils
import com.depromeet.threedollar.api.boss.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.api.core.service.bossservice.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.NewBossAppliedRegistrationEvent

@Service
class SignupService(
    private val bossAccountRepository: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository,
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @Transactional
    fun signUp(request: SignupRequest, socialId: String): String {
        BossAccountServiceUtils.validateNotExistsBossAccount(bossAccountRepository, socialId, request.socialType)
        validateDuplicateRegistration(socialId, request.socialType)
        BossStoreCategoryServiceUtils.validateExistsCategories(bossStoreCategoryRepository, request.storeCategoriesIds)

        val registration = request.toEntity(socialId)
        bossRegistrationRepository.save(registration)

        eventPublisher.publishEvent(NewBossAppliedRegistrationEvent.of(registration))
        return registration.id
    }

    private fun validateDuplicateRegistration(socialId: String, socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType) {
        if (bossRegistrationRepository.existsWaitingRegistrationBySocialIdAndSocialType(socialId, socialType)) {
            throw ForbiddenException("가입 승인 대기중인 사장님 게정(${socialId} - (${socialType}) 입니다.", ErrorCode.FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT)
        }
    }

}
