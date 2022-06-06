package com.depromeet.threedollar.domain.mongo.domain.bossservice.account.repository

interface BossAccountRepositoryCustom {

    fun existsBossAccountById(id: String): Boolean

    fun findBossAccountById(id: String): com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount?

    fun findBossAccountBySocialInfo(socialId: String, socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType): com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount?

    fun existsBossAccountBySocialInfo(socialId: String, socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType): Boolean

}
