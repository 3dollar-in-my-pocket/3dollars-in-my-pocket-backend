package com.depromeet.threedollar.boss.api.service.auth

import com.depromeet.threedollar.boss.api.service.account.BossAccountServiceUtils
import com.depromeet.threedollar.boss.api.service.category.BossStoreCategoryServiceUtils
import com.depromeet.threedollar.boss.api.service.auth.dto.request.SignupRequest
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.boss.document.category.BossStoreCategoryRepository
import com.depromeet.threedollar.document.boss.document.registration.RegistrationRepository
import com.depromeet.threedollar.document.boss.document.account.BossWithdrawalAccount
import com.depromeet.threedollar.document.boss.document.account.BossWithdrawalAccountRepository
import com.depromeet.threedollar.document.boss.event.registration.BossSignOutEvent
import com.depromeet.threedollar.document.boss.event.registration.NewBossAppliedRegistrationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthCommonService(
    private val bossAccountRepository: BossAccountRepository,
    private val bossWithdrawalAccountRepository: BossWithdrawalAccountRepository,
    private val registrationRepository: RegistrationRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    fun signUp(request: SignupRequest, socialId: String) {
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

    @Transactional
    fun signOut(bossId: String) {
        val bossAccount = BossAccountServiceUtils.findBossAccountById(bossAccountRepository, bossId)
        bossWithdrawalAccountRepository.save(BossWithdrawalAccount.newInstance(bossAccount))
        bossAccountRepository.delete(bossAccount)

        eventPublisher.publishEvent(BossSignOutEvent.of(bossId))
    }

}
