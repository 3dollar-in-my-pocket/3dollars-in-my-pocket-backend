package com.depromeet.threedollar.document.boss.document.account.repository

import com.depromeet.threedollar.document.boss.document.account.BossAccount
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType

interface BossAccountRepositoryCustom {

    fun findBossAccountById(id: String): BossAccount?

    fun findBossAccountBySocialInfo(socialId: String, socialType: BossAccountSocialType): BossAccount?

}
