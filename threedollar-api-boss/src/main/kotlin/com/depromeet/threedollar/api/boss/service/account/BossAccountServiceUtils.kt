package com.depromeet.threedollar.api.boss.service.account

import com.depromeet.threedollar.common.exception.model.ConflictException
import com.depromeet.threedollar.common.exception.model.ForbiddenException
import com.depromeet.threedollar.common.exception.model.NotFoundException
import com.depromeet.threedollar.common.exception.type.ErrorCode
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationRepository

object BossAccountServiceUtils {

    fun findBossAccountById(bossAccountRepository: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository, bossId: String): com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount {
        return bossAccountRepository.findBossAccountById(bossId)
            ?: throw NotFoundException("해당하는 id($bossId)를 가진 사장님은 존재하지 않습니다", ErrorCode.NOTFOUND_BOSS)
    }

    fun validateNotExistsBossAccount(
            bossAccountRepository: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository,
            socialId: String,
            socialType: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
    ) {
        if (bossAccountRepository.existsBossAccountBySocialInfo(socialId, socialType)) {
            throw ConflictException("이미 가입한 사장님 (${socialId} - $socialType 입니다.", ErrorCode.CONFLICT_USER)
        }
    }

    fun findBossAccountBySocialIdAndSocialTypeWithCheckWaitingRegistration(
            bossAccountRepository: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountRepository,
            registrationRepository: RegistrationRepository,
            socialId: String,
            socialType: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
    ): com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount {
        return bossAccountRepository.findBossAccountBySocialInfo(socialId, socialType)
            ?: checkIsWaitingRegistration(registrationRepository, socialId, socialType)
    }

    private fun checkIsWaitingRegistration(
        registrationRepository: RegistrationRepository,
        socialId: String,
        socialType: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
    ): com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount {
        if (registrationRepository.existsRegistrationBySocialIdAndSocialType(socialId, socialType)) {
            throw ForbiddenException("가입 신청 후 대기중인 사장님(${socialId} - (${socialType}) 입니다.", ErrorCode.FORBIDDEN_WAITING_APPROVE_BOSS_ACCOUNT)
        }
        throw NotFoundException("존재하지 않는 사장님 (${socialId} - $socialType 입니다.", ErrorCode.NOTFOUND_BOSS)
    }

}
