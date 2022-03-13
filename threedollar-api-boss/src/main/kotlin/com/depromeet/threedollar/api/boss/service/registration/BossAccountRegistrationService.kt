package com.depromeet.threedollar.api.boss.service.registration

import com.depromeet.threedollar.api.boss.service.account.BossAccountServiceUtils
import com.depromeet.threedollar.api.boss.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.api.core.service.boss.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository
import com.depromeet.threedollar.domain.mongo.boss.event.registration.NewBossAppliedRegistrationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossAccountRegistrationService(
    private val bossAccountRepository: BossAccountRepository,
    private val registrationRepository: RegistrationRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun applyForBossAccountRegistration(request: SignupRequest, socialId: String) {
        validateDuplicateRegistration(socialId, request.socialType)
        BossStoreCategoryServiceUtils.validateExistsCategories(bossStoreCategoryRepository, request.storeCategoriesIds)
        val registration = request.toEntity(socialId)
        registrationRepository.save(registration)

        eventPublisher.publishEvent(NewBossAppliedRegistrationEvent.of(registration))
    }

    private fun validateDuplicateRegistration(socialId: String, socialType: BossAccountSocialType) {
        BossAccountServiceUtils.validateNotExistsBossAccount(bossAccountRepository, socialId, socialType)
        if (registrationRepository.existsRegistrationBySocialIdAndSocialType(socialId, socialType)) {
            throw ForbiddenException("가입 신청 후 대기중인 사장님(${socialId} - (${socialType}) 입니다.", ErrorCode.FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT)
        }
    }

}
