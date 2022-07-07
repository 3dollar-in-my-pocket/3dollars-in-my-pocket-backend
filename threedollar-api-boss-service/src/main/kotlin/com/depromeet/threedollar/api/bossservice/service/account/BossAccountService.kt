package com.depromeet.threedollar.api.bossservice.service.account

import com.depromeet.threedollar.api.bossservice.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.api.bossservice.service.account.dto.response.BossAccountInfoResponse
import com.depromeet.threedollar.domain.mongo.config.mongo.MongoTransactional
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossWithdrawalAccount
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossWithdrawalAccountRepository
import com.depromeet.threedollar.domain.mongo.event.bossservice.registration.BossSignOutEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class BossAccountService(
    private val bossAccountRepository: BossAccountRepository,
    private val bossWithdrawalAccountRepository: BossWithdrawalAccountRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun getBossAccountInfo(bossId: String): BossAccountInfoResponse {
        val bossAccount = BossAccountServiceHelper.findBossAccountByRegistrationId(bossAccountRepository, bossId)
        return BossAccountInfoResponse.of(bossAccount)
    }

    fun updateBossAccountInfo(bossId: String, request: UpdateBossAccountInfoRequest) {
        val bossAccount = BossAccountServiceHelper.findBossAccountByRegistrationId(bossAccountRepository, bossId)
        bossAccount.updateInfo(request.name, request.isSetupNotification)
        bossAccountRepository.save(bossAccount)
    }

    @MongoTransactional
    fun signOut(bossId: String) {
        val bossAccount = BossAccountServiceHelper.findBossAccountByRegistrationId(bossAccountRepository, bossId)
        bossWithdrawalAccountRepository.save(BossWithdrawalAccount.newInstance(bossAccount))
        bossAccountRepository.delete(bossAccount)

        eventPublisher.publishEvent(BossSignOutEvent.of(bossId))
    }

}

