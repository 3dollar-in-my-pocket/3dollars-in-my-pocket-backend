package com.depromeet.threedollar.boss.api.service.account

import com.depromeet.threedollar.boss.api.service.account.dto.response.BossAccountInfoResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.domain.boss.domain.account.BossAccount
import com.depromeet.threedollar.domain.boss.domain.account.BossAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossAccountService(
    private val bossAccountRepository: BossAccountRepository
) {

    @Transactional(readOnly = true)
    fun getBossAccountInfo(bossAccountId: Long): BossAccountInfoResponse {
        val bossAccount = findBossAccountById(bossAccountRepository, bossAccountId)
        return BossAccountInfoResponse.of(bossAccount)
    }

}

fun findBossAccountById(bossAccountRepository: BossAccountRepository, bossAccountId: Long): BossAccount {
    return bossAccountRepository.findBossAccountById(bossAccountId)
        ?: throw NotFoundException("해당하는 id($bossAccountId)를 가진 사장님은 존재하지 않습니다")
}
