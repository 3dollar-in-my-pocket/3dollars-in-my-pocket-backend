package com.depromeet.threedollar.domain.mongo.domain.bossservice.account.repository

import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType

interface BossAccountRepositoryCustom {

    fun existsBossAccountById(id: String): Boolean

    fun findBossAccountById(id: String): BossAccount?

    fun findBossAccountBySocialInfo(socialId: String, socialType: BossAccountSocialType): BossAccount?

    fun existsBossAccountBySocialInfo(socialId: String, socialType: BossAccountSocialType): Boolean

}
