package com.depromeet.threedollar.domain.mongo.boss.domain.account.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo

class BossAccountRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossAccountRepositoryCustom {

    override fun findBossAccountById(id: String): com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount? {
        return mongoTemplate.findOne(Query()
            .addCriteria(com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount::id isEqualTo id)
        )
    }

    override fun findBossAccountBySocialInfo(socialId: String, socialType: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType): com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount? {
        return mongoTemplate.findOne(Query()
            .addCriteria(com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount::socialInfo isEqualTo com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo.of(socialId, socialType))
        )
    }

    override fun existsBossAccountBySocialInfo(socialId: String, socialType: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount::socialInfo isEqualTo com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialInfo.of(socialId, socialType)), com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccount::class.java
        )
    }

}
