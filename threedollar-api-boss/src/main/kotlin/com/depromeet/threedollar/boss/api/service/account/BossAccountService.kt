package com.depromeet.threedollar.boss.api.service.account

import com.depromeet.threedollar.boss.api.service.account.dto.request.UpdateBossAccountInfoRequest
import com.depromeet.threedollar.boss.api.service.account.dto.response.BossAccountInfoResponse
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossAccountService(
    private val bossAccountRepository: BossAccountRepository
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

}

