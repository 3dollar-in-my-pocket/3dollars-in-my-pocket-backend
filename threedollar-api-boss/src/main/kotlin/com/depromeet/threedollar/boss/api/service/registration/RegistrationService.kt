package com.depromeet.threedollar.boss.api.service.registration

import com.depromeet.threedollar.boss.api.service.account.validateNotExistsBossAccount
import com.depromeet.threedollar.boss.api.service.category.validateExistsCategories
import com.depromeet.threedollar.boss.api.service.registration.dto.request.ApplyRegistrationRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import com.depromeet.threedollar.document.boss.document.registration.RegistrationRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegistrationService(
    private val bossAccountRepository: BossAccountRepository,
    private val registrationRepository: RegistrationRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun applyRegistration(
        request: ApplyRegistrationRequest,
        socialId: String
    ) {
        validateDuplicateRegistration(socialId, request.socialType)
        validateExistsCategories(bossStoreCategoryRepository, request.storeCategoriesIds)
        val registration = request.toEntity(socialId)
        registrationRepository.save(registration)

        eventPublisher.publishEvent(registration)
    }

    private fun validateDuplicateRegistration(socialId: String, socialType: BossAccountSocialType) {
        validateNotExistsBossAccount(bossAccountRepository, socialId, socialType)

        if (registrationRepository.findRegistrationBySocialInfo(socialId, socialType) != null) {
            throw ConflictException("이미 가입 신청한 사장님 (${socialId} - (${socialType}) 입니다.")
        }
    }

}
