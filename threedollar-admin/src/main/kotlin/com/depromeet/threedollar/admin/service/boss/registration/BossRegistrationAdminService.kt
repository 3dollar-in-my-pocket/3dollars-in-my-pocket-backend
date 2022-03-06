package com.depromeet.threedollar.admin.service.boss.registration

import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.document.boss.document.account.BossAccount
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialInfo
import com.depromeet.threedollar.document.boss.document.registration.Registration
import com.depromeet.threedollar.document.boss.document.registration.RegistrationRepository
import com.depromeet.threedollar.document.boss.document.store.BossStoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossRegistrationAdminService(
    private val registrationRepository: RegistrationRepository,
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreRepository: BossStoreRepository
) {

    @Transactional
    fun applyBossRegistration(registrationId: String) {
        val registration = BossRegistrationServiceUtils.findWaitingRegistrationById(registrationRepository, registrationId)
        val bossAccount = registerNewBossAccount(registration)
        bossStoreRepository.save(registration.toBossStore(bossAccount.id))
        registration.approve()
        registrationRepository.save(registration)
    }

    private fun registerNewBossAccount(registration: Registration): BossAccount {
        validateDuplicateRegistration(registration.boss.socialInfo)
        return bossAccountRepository.save(registration.toBossAccount())
    }

    private fun validateDuplicateRegistration(socialInfo: BossAccountSocialInfo) {
        if (bossAccountRepository.existsBossAccountBySocialInfo(socialId = socialInfo.socialId, socialType = socialInfo.socialType)) {
            throw ConflictException("이미 가입한 사장님(${socialInfo.socialId} - ${socialInfo.socialType})입니다", ErrorCode.CONFLICT_EXISTS_BOSS)
        }
    }

    @Transactional
    fun rejectBossRegistration(registrationId: String) {
        val registration = BossRegistrationServiceUtils.findWaitingRegistrationById(registrationRepository, registrationId)
        registration.reject()
        registrationRepository.save(registration)
    }

}
