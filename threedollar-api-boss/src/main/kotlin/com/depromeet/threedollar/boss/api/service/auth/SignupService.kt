package com.depromeet.threedollar.boss.api.service.auth

import com.depromeet.threedollar.boss.api.service.account.BossAccountServiceUtils
import com.depromeet.threedollar.boss.api.service.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.boss.api.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import com.depromeet.threedollar.document.boss.document.registration.RegistrationRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SignupService(
    private val bossAccountRepository: BossAccountRepository,
    private val registrationRepository: RegistrationRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun signup(
        request: SignupRequest,
        socialId: String
    ) {
        validateDuplicateRegistration(socialId, request.socialType)
        BossStoreCategoryServiceUtils.validateExistsCategories(bossStoreCategoryRepository, request.storeCategoriesIds)
        val registration = request.toEntity(socialId)
        registrationRepository.save(registration)

        eventPublisher.publishEvent(registration)
    }

    private fun validateDuplicateRegistration(socialId: String, socialType: BossAccountSocialType) {
        BossAccountServiceUtils.validateNotExistsBossAccount(bossAccountRepository, socialId, socialType)

        if (registrationRepository.findRegistrationBySocialInfo(socialId, socialType) != null) {
            throw ConflictException("이미 가입 신청한 사장님 (${socialId} - (${socialType}) 입니다.", ErrorCode.CONFLICT_REGISTER_BOSS)
        }
    }

}
