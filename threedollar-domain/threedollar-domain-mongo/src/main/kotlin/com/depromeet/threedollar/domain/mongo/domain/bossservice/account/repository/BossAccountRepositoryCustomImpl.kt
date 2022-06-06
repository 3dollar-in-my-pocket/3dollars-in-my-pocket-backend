package com.depromeet.threedollar.domain.mongo.domain.bossservice.account.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class BossAccountRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
) : com.depromeet.threedollar.domain.mongo.domain.bossservice.account.repository.BossAccountRepositoryCustom {

    override fun existsBossAccountById(id: String): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount::id isEqualTo id), com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount::class.java
        )
    }

    override fun findBossAccountById(id: String): com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount? {
        return mongoTemplate.findOne(Query()
            .addCriteria(com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount::id isEqualTo id)
        )
    }

    override fun findBossAccountBySocialInfo(socialId: String, socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType): com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount? {
        return mongoTemplate.findOne(Query()
            .addCriteria(com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount::socialInfo isEqualTo com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialInfo.of(socialId, socialType))
        )
    }

    override fun existsBossAccountBySocialInfo(socialId: String, socialType: com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount::socialInfo isEqualTo com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialInfo.of(socialId, socialType)), com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount::class.java
        )
    }

}
