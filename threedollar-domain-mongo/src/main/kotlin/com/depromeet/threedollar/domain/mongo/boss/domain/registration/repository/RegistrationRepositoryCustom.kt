package com.depromeet.threedollar.domain.mongo.boss.domain.registration.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.Registration

interface RegistrationRepositoryCustom {

    fun existsRegistrationBySocialIdAndSocialType(socialId: String, socialType: BossAccountSocialType): Boolean

    fun findWaitingRegistrationById(registrationId: String): Registration?

}
