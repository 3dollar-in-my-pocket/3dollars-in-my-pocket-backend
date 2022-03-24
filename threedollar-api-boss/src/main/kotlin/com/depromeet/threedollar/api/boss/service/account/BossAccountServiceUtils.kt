package com.depromeet.threedollar.api.boss.service.account

import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository

object BossAccountServiceUtils {

    fun findBOssAccountByRegistrationId(bossAccountRepository: BossAccountRepository, bossId: String): BossAccount {
        return bossAccountRepository.findBossAccountById(bossId)
            ?: throw NotFoundException("해당하는 id($bossId)를 가진 사장님은 존재하지 않습니다", ErrorCode.NOTFOUND_BOSS)
    }

    fun validateNotExistsBossAccount(
        bossAccountRepository: BossAccountRepository,
        socialId: String,
        socialType: BossAccountSocialType
    ) {
        if (bossAccountRepository.existsBossAccountBySocialInfo(socialId, socialType)) {
            throw ConflictException("이미 가입한 사장님 (${socialId} - $socialType 입니다.", ErrorCode.CONFLICT_USER)
        }
    }

    fun findBossAccountBySocialIdAndSocialTypeWithCheckWaitingRegistration(
        bossAccountRepository: BossAccountRepository,
        registrationRepository: RegistrationRepository,
        socialId: String,
        socialType: BossAccountSocialType
    ): String {
        return bossAccountRepository.findBossAccountBySocialInfo(socialId, socialType)?.id
            ?: return registrationRepository.findWaitingRegistrationBySocialIdAndSocialType(socialId, socialType)?.id
                ?: throw NotFoundException("존재하지 않는 사장님 (${socialId} - $socialType 입니다.", ErrorCode.NOTFOUND_BOSS)
    }

}
