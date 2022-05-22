package com.depromeet.threedollar.domain.mongo.boss.domain.registration.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistration

interface BossRegistrationRepositoryCustom {

    fun existsWaitingRegistrationBySocialIdAndSocialType(socialId: String, socialType: BossAccountSocialType): Boolean

    fun findWaitingRegistrationById(registrationId: String): BossRegistration?

    fun findWaitingRegistrationBySocialIdAndSocialType(socialId: String, socialType: BossAccountSocialType): BossRegistration?

    fun findAllWaitingRegistrationsLessThanCursorOrderByLatest(cursor: String?, size: Int): List<BossRegistration>

}
