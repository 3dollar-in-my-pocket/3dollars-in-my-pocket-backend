package com.depromeet.threedollar.domain.mongo.boss.domain.account.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo

class BossAccountRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossAccountRepositoryCustom {

    override fun findBossAccountById(id: String): BossAccount? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossAccount::id isEqualTo id)
        )
    }

    override fun findBossAccountBySocialInfo(socialId: String, socialType: BossAccountSocialType): BossAccount? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossAccount::socialInfo isEqualTo BossAccountSocialInfo.of(socialId, socialType))
        )
    }

    override fun existsBossAccountBySocialInfo(socialId: String, socialType: BossAccountSocialType): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(BossAccount::socialInfo isEqualTo BossAccountSocialInfo.of(socialId, socialType)), BossAccount::class.java
        )
    }

}
