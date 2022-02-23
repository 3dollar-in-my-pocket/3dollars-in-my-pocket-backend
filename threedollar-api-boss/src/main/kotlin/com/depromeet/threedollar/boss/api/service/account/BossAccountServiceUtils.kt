package com.depromeet.threedollar.boss.api.service.account

import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.document.boss.document.account.BossAccount
import com.depromeet.threedollar.document.boss.document.account.BossAccountRepository
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.boss.document.registration.RegistrationRepository

object BossAccountServiceUtils {

    fun findBossAccountById(bossAccountRepository: BossAccountRepository, bossId: String): BossAccount {
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
    ): BossAccount {
        return bossAccountRepository.findBossAccountBySocialInfo(socialId, socialType)
            ?: checkIsWaitingRegistration(registrationRepository, socialId, socialType)
    }

    private fun checkIsWaitingRegistration(
        registrationRepository: RegistrationRepository,
        socialId: String,
        socialType: BossAccountSocialType
    ): BossAccount {
        if (registrationRepository.existsRegistrationBySocialIdAndSocialType(socialId, socialType)) {
            throw ForbiddenException("가입 신청 후 대기중인 사장님(${socialId} - (${socialType}) 입니다.", ErrorCode.FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT)
        }
        throw NotFoundException("존재하지 않는 사장님 (${socialId} - $socialType 입니다.", ErrorCode.NOTFOUND_BOSS)
    }

}
