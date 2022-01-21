package com.depromeet.threedollar.boss.api.service.account

import com.depromeet.threedollar.boss.api.service.account.dto.response.BossAccountInfoResponse
import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.document.boss.document.account.BossAccount
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
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

fun findBossAccountBySocialIdAndSocialType(bossAccountRepository: BossAccountRepository, socialId: String, socialType: BossAccountSocialType): BossAccount {
    return bossAccountRepository.findBossAccountBySocialInfo(socialId, socialType)
        ?: throw NotFoundException("존재하지 않는 사장님 (${socialId} - $socialType 입니다.")
}

fun validateNotExistsBossAccount(bossAccountRepository: BossAccountRepository, socialId: String, socialType: BossAccountSocialType) {
    if (bossAccountRepository.findBossAccountBySocialInfo(socialId, socialType) != null) {
        throw ConflictException("이미 가입한 사장님 (${socialId} - $socialType 입니다.")
    }
}
