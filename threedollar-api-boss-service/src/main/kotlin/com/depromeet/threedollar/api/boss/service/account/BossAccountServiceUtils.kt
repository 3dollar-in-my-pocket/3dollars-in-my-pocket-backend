package com.depromeet.threedollar.api.boss.service.account

import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistrationRepository

object BossAccountServiceUtils {

    fun findBossAccountByRegistrationId(
        bossAccountRepository: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository,
        bossId: String,
    ): com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount {
        return bossAccountRepository.findBossAccountById(bossId)
            ?: throw NotFoundException("해당하는 사장님 계정($bossId)은 존재하지 않습니다", ErrorCode.NOT_FOUND_BOSS_ACCOUNT)
    }

    fun validateNotExistsBossAccount(
        bossAccountRepository: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository,
        socialId: String,
        socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType,
    ) {
        if (bossAccountRepository.existsBossAccountBySocialInfo(socialId, socialType)) {
            throw ConflictException("이미 가입한 사장님 계정(${socialId} - $socialType 입니다.", ErrorCode.CONFLICT_BOSS_ACCOUNT)
        }
    }

    fun findBossAccountBySocialIdAndSocialTypeWithCheckWaitingRegistration(
        bossAccountRepository: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountRepository,
        bossRegistrationRepository: BossRegistrationRepository,
        socialId: String,
        socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType,
    ): String {
        val bossAccount = bossAccountRepository.findBossAccountBySocialInfo(socialId, socialType)
        if (bossAccount != null) {
            return bossAccount.id
        }
        val bossRegistration = bossRegistrationRepository.findWaitingRegistrationBySocialIdAndSocialType(socialId, socialType)
            ?: throw NotFoundException("존재하지 않는 사장님 계정(${socialId} - $socialType 입니다.", ErrorCode.NOT_FOUND_BOSS_ACCOUNT)
        return bossRegistration.id
    }

}
