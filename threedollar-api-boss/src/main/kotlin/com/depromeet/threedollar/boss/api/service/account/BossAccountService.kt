package com.depromeet.threedollar.boss.api.service.account

import com.depromeet.threedollar.boss.api.service.account.dto.response.BossAccountInfoResponse
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.document.boss.document.account.BossAccount
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BossAccountService(
    private val bossAccountRepository: BossAccountRepository
) {

    @Transactional(readOnly = true)
    fun getBossAccountInfo(bossId: String): BossAccountInfoResponse {
        val bossAccount = findBossAccountById(bossAccountRepository, bossId)
        return BossAccountInfoResponse.of(bossAccount)
    }

}

fun findBossAccountById(bossAccountRepository: BossAccountRepository, bossId: String): BossAccount {
    return bossAccountRepository.findBossAccountById(bossId)
        ?: throw NotFoundException("해당하는 id($bossId)를 가진 사장님은 존재하지 않습니다")
}
