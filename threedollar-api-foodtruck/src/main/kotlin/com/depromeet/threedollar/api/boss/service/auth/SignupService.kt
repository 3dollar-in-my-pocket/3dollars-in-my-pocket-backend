package com.depromeet.threedollar.api.boss.service.auth

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.boss.service.account.BossAccountServiceUtils
import com.depromeet.threedollar.api.boss.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.api.core.service.foodtruck.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.foodtruck.domain.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.foodtruck.event.registration.NewBossAppliedRegistrationEvent

@Service
class SignupService(
    private val bossAccountRepository: BossAccountRepository,
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

    private fun validateDuplicateRegistration(socialId: String, socialType: BossAccountSocialType) {
        if (bossRegistrationRepository.existsWaitingRegistrationBySocialIdAndSocialType(socialId, socialType)) {
            throw ForbiddenException("가입 승인 대기중인 사장님 게정(${socialId} - (${socialType}) 입니다.", ErrorCode.FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT)
        }
    }

}
