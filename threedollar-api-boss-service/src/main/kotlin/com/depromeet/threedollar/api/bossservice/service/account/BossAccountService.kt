package com.depromeet.threedollar.api.bossservice.service.account

import com.depromeet.threedollar.api.bossservice.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.api.bossservice.service.account.dto.response.BossAccountInfoResponse
import com.depromeet.threedollar.domain.mongo.config.mongo.MongoTransactional
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossWithdrawalAccount
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossWithdrawalAccountRepository
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.AccountType
import com.depromeet.threedollar.domain.mongo.domain.commonservice.device.DeviceRepository
import com.depromeet.threedollar.domain.mongo.event.bossservice.account.BossSignOutEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class BossAccountService(
    private val bossAccountRepository: BossAccountRepository,
    private val bossWithdrawalAccountRepository: BossWithdrawalAccountRepository,
    private val deviceRepository: DeviceRepository,
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun getBossAccountInfo(bossId: String): BossAccountInfoResponse {
        return BossAccountInfoResponse.of(
            bossAccount = BossAccountServiceHelper.findBossAccountByRegistrationId(bossAccountRepository, bossId),
            isSetupNotification = deviceRepository.existsDeviceByAccountIdAndType(accountId = bossId, accountType = AccountType.BOSS_ACCOUNT),
        )
    }

    fun updateBossAccountInfo(bossId: String, request: UpdateBossAccountInfoRequest) {
        val bossAccount = BossAccountServiceHelper.findBossAccountByRegistrationId(bossAccountRepository, bossId)
        bossAccount.updateInfo(request.name)
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

