package com.depromeet.threedollar.api.admin.service.boss.registration

import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.Registration
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossRegistrationAdminService(
        private val registrationRepository: RegistrationRepository,
        private val bossAccountRepository: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository,
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

    private fun registerNewBossAccount(registration: Registration): com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount {
        validateDuplicateRegistration(registration.boss.socialInfo)
        return bossAccountRepository.save(registration.toBossAccount())
    }

    private fun validateDuplicateRegistration(socialInfo: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo) {
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