package com.depromeet.threedollar.api.adminservice.service.bossservice.registration

import com.depromeet.threedollar.api.adminservice.service.bossservice.registration.dto.request.RetrieveBossRegistrationsRequest
import com.depromeet.threedollar.api.adminservice.service.bossservice.registration.dto.response.BossAccountRegistrationResponse
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.config.mongo.MongoTransactional
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategory
import com.depromeet.threedollar.domain.mongo.domain.bossservice.category.BossStoreCategoryRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistration
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistrationRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.store.BossStoreRepository
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.BossRegistrationApprovedEvent
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.BossRegistrationDeniedEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class BossRegistrationAdminService(
    private val bossRegistrationRepository: BossRegistrationRepository,
    private val bossAccountRepository: BossAccountRepository,
    private val bossStoreRepository: BossStoreRepository,
    private val bossStoreCategoryRepository: BossStoreCategoryRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    @MongoTransactional
    fun applyBossRegistration(registrationId: String) {
        val registration = BossRegistrationServiceHelper.findWaitingRegistrationById(bossRegistrationRepository, registrationId)
        val bossAccount = registerNewBossAccount(registration)
        bossStoreRepository.save(registration.toBossStore(bossAccount.id))
        registration.approve()
        bossRegistrationRepository.save(registration)

        eventPublisher.publishEvent(BossRegistrationApprovedEvent(bossRegistrationId = registrationId))
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

    fun rejectBossRegistration(registrationId: String) {
        val registration = BossRegistrationServiceHelper.findWaitingRegistrationById(bossRegistrationRepository, registrationId)
        registration.reject()
        bossRegistrationRepository.save(registration)

        eventPublisher.publishEvent(BossRegistrationDeniedEvent(bossRegistrationId = registrationId))
    }

    fun retrieveBossRegistrations(request: RetrieveBossRegistrationsRequest): List<BossAccountRegistrationResponse> {
        val registrations = bossRegistrationRepository.findAllWaitingRegistrationsLessThanCursorOrderByLatest(request.cursor, request.size)
        val bossStoreCategoryMap: Map<String, BossStoreCategory> = bossStoreCategoryRepository.findAll()
            .associateBy { it.id }
        return registrations.map { registration ->
            BossAccountRegistrationResponse.of(bossRegistration = registration, bossStoreCategoryMap = bossStoreCategoryMap)
        }
    }

}
