package com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistration

interface BossRegistrationRepositoryCustom {

    fun existsWaitingRegistrationBySocialIdAndSocialType(socialId: String, socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType): Boolean

    fun findWaitingRegistrationById(registrationId: String): BossRegistration?

    fun findWaitingRegistrationBySocialIdAndSocialType(socialId: String, socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType): BossRegistration?

    fun findAllWaitingRegistrationsLessThanCursorOrderByLatest(cursor: String?, size: Int): List<BossRegistration>

}
