package com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.repository

import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.BossRegistration

interface BossRegistrationRepositoryCustom {

    fun existsWaitingRegistrationBySocialIdAndSocialType(socialId: String, socialType: BossAccountSocialType): Boolean

    fun findWaitingRegistrationById(registrationId: String): BossRegistration?

    fun existsWaitingRegistrationById(registrationId: String): Boolean

    fun findWaitingRegistrationBySocialIdAndSocialType(socialId: String, socialType: BossAccountSocialType): BossRegistration?

    fun findAllWaitingRegistrationsLessThanCursorOrderByLatest(cursor: String?, size: Int): List<BossRegistration>

}
