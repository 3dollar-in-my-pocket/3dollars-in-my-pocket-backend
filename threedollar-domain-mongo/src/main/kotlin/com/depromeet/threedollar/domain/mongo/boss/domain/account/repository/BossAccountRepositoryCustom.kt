package com.depromeet.threedollar.domain.mongo.boss.domain.account.repository

interface BossAccountRepositoryCustom {

    fun findBossAccountById(id: String): com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount?

    fun findBossAccountBySocialInfo(socialId: String, socialType: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType): com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount?

    fun existsBossAccountBySocialInfo(socialId: String, socialType: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType): Boolean

}
