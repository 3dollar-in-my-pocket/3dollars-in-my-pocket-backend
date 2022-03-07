package com.depromeet.threedollar.api.boss.service.account

import com.depromeet.threedollar.api.boss.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.api.boss.service.account.dto.response.BossAccountInfoResponse
import com.depromeet.threedollar.domain.mongo.boss.event.registration.BossSignOutEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossAccountService(
        private val bossAccountRepository: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository,
        private val bossWithdrawalAccountRepository: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossWithdrawalAccountRepository,
        private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional(readOnly = true)
    fun getBossAccountInfo(bossId: String): BossAccountInfoResponse {
        val bossAccount = BossAccountServiceUtils.findBossAccountById(bossAccountRepository, bossId)
        return BossAccountInfoResponse.of(bossAccount)
    }

    @Transactional
    fun updateBossAccountInfo(bossId: String, request: UpdateBossAccountInfoRequest) {
        val bossAccount = BossAccountServiceUtils.findBossAccountById(bossAccountRepository, bossId)
        request.let {
            bossAccount.update(it.name, it.pushSettingsStatus)
        }
        bossAccountRepository.save(bossAccount)
    }

    @Transactional
    fun signOut(bossId: String) {
        val bossAccount = BossAccountServiceUtils.findBossAccountById(bossAccountRepository, bossId)
        bossWithdrawalAccountRepository.save(com.depromeet.threedollar.domain.mongo.boss.domain.account.BossWithdrawalAccount.newInstance(bossAccount))
        bossAccountRepository.delete(bossAccount)

        eventPublisher.publishEvent(BossSignOutEvent.of(bossId))
    }

}

