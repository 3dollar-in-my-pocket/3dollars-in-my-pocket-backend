package com.depromeet.threedollar.document.boss.document.account

import org.springframework.data.repository.CrudRepository

interface BossAccountRepository : CrudRepository<BossAccount, String> {

    fun findBossAccountById(id: String): BossAccount?

    fun findBossAccountBySocialInfo(socialInfo: SocialInfo): BossAccount?

}
