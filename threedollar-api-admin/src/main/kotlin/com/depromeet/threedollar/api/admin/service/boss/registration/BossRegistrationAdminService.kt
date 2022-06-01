package com.depromeet.threedollar.api.admin.service.boss.registration

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.admin.service.boss.registration.dto.request.RetrieveBossRegistrationsRequest
import com.depromeet.threedollar.api.admin.service.boss.registration.dto.response.BossAccountRegistrationResponse
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistration
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository

@Service
class BossRegistrationAdminService(
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
) {

    @Transactional
    fun applyBossRegistration(registrationId: String) {
        val registration = BossRegistrationServiceUtils.findWaitingRegistrationById(bossRegistrationRepository, registrationId)
        val bossAccount = registerNewBossAccount(registration)
        bossStoreRepository.save(registration.toBossStore(bossAccount.id))
        registration.approve()
        bossRegistrationRepository.save(registration)
    }

    private fun registerNewBossAccount(bossRegistration: BossRegistration): BossAccount {
        validateDuplicateRegistration(bossRegistration.boss.socialInfo)
        return bossAccountRepository.save(bossRegistration.toBossAccount())
    }

    private fun validateDuplicateRegistration(socialInfo: BossAccountSocialInfo) {
        if (bossAccountRepository.existsBossAccountBySocialInfo(socialId = socialInfo.socialId, socialType = socialInfo.socialType)) {
            throw ConflictException("이미 가입한 사장님(${socialInfo.socialId} - ${socialInfo.socialType})입니다", ErrorCode.CONFLICT_BOSS_ACCOUNT)
        }
    }

    @Transactional
    fun rejectBossRegistration(registrationId: String) {
        val registration = BossRegistrationServiceUtils.findWaitingRegistrationById(bossRegistrationRepository, registrationId)
        registration.reject()
        bossRegistrationRepository.save(registration)
    }

    @Transactional(readOnly = true)
    fun retrieveBossRegistrations(request: RetrieveBossRegistrationsRequest): List<BossAccountRegistrationResponse> {
        val registrations = bossRegistrationRepository.findAllWaitingRegistrationsLessThanCursorOrderByLatest(request.cursor, request.size)
        val bossStoreCategoryMap: Map<String, BossStoreCategory> = bossStoreCategoryRepository.findAll()
            .associateBy { it.id }
        return registrations.map { registration ->
            BossAccountRegistrationResponse.of(bossRegistration = registration, bossStoreCategoryMap = bossStoreCategoryMap)
        }
    }

}
