package com.depromeet.threedollar.api.admin.service.boss.registration

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.admin.service.boss.registration.dto.request.GetBossRegistrationsRequest
import com.depromeet.threedollar.api.admin.service.boss.registration.dto.response.BossAccountRegistrationResponse
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.boss.domain.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.Registration
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.store.BossStoreRepository

@Service
class BossAccountRegistrationAdminService(
    private val registrationRepository: RegistrationRepository,
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository
) {

    @Transactional
    fun applyBossRegistration(registrationId: String) {
        val registration = BossAccountRegistrationServiceUtils.findWaitingRegistrationById(registrationRepository, registrationId)
        val bossAccount = registerNewBossAccount(registration)
        bossStoreRepository.save(registration.toBossStore(bossAccount.id))
        registration.approve()
        registrationRepository.save(registration)
        // TODO 가입 승인 슬랙 알림
    }

    private fun registerNewBossAccount(registration: Registration): BossAccount {
        validateDuplicateRegistration(registration.boss.socialInfo)
        return bossAccountRepository.save(registration.toBossAccount())
    }

    private fun validateDuplicateRegistration(socialInfo: BossAccountSocialInfo) {
        if (bossAccountRepository.existsBossAccountBySocialInfo(socialId = socialInfo.socialId, socialType = socialInfo.socialType)) {
            throw ConflictException("이미 가입한 사장님(${socialInfo.socialId} - ${socialInfo.socialType})입니다", ErrorCode.CONFLICT_BOSS_ACCOUNT)
        }
    }

    @Transactional
    fun rejectBossRegistration(registrationId: String) {
        val registration = BossAccountRegistrationServiceUtils.findWaitingRegistrationById(registrationRepository, registrationId)
        registration.reject()
        registrationRepository.save(registration)
        // TODO 가입 거부 슬랙 알림
    }

    @Transactional(readOnly = true)
    fun getBossRegistrations(request: GetBossRegistrationsRequest): List<BossAccountRegistrationResponse> {
        val registrations = registrationRepository.findAllWaitingRegistrationsLessThanCursorOrderByLatest(request.cursor, request.size)
        val bossStoreCategoryMap: Map<String, BossStoreCategory> = bossStoreCategoryRepository.findAll().associateBy { it.id }
        return registrations.map {
            BossAccountRegistrationResponse.of(it, bossStoreCategoryMap)
        }
    }

}
