package com.depromeet.threedollar.api.boss.service.account

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.depromeet.threedollar.api.boss.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.api.boss.service.account.dto.response.BossAccountInfoResponse
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossWithdrawalAccount
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossWithdrawalAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.event.registration.BossSignOutEvent

@Service
class BossAccountService(
    private val bossAccountRepository: BossAccountRepository,
    private val bossWithdrawalAccountRepository: BossWithdrawalAccountRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional(readOnly = true)
    fun getBossAccountInfo(bossId: String): BossAccountInfoResponse {
        val bossAccount = BossAccountServiceUtils.findBossAccountByRegistrationId(bossAccountRepository, bossId)
        return BossAccountInfoResponse.of(bossAccount)
    }

    @Transactional
    fun updateBossAccountInfo(bossId: String, request: UpdateBossAccountInfoRequest) {
        val bossAccount = BossAccountServiceUtils.findBossAccountByRegistrationId(bossAccountRepository, bossId)
        request.let {
            bossAccount.updateInfo(it.name, it.isSetupNotification)
        }
        bossAccountRepository.save(bossAccount)
    }

    @Transactional
    fun signOut(bossId: String) {
        val bossAccount = BossAccountServiceUtils.findBossAccountByRegistrationId(bossAccountRepository, bossId)
        bossWithdrawalAccountRepository.save(BossWithdrawalAccount.newInstance(bossAccount))
        bossAccountRepository.delete(bossAccount)

        eventPublisher.publishEvent(BossSignOutEvent.of(bossId))
    }

}

