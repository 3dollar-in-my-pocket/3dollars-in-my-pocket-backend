package com.depromeet.threedollar.domain.mongo.domain.bossservice.account.repository

import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccount
import com.depromeet.threedollar.domain.mongo.domain.bossservice.account.BossAccountSocialType
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class BossAccountRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate,
) : BossAccountRepositoryCustom {

    override fun existsBossAccountById(id: String): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(BossAccount::id isEqualTo id), BossAccount::class.java
        )
    }

    override fun findBossAccountById(id: String): BossAccount? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossAccount::id isEqualTo id)
        )
    }

    override fun findBossAccountBySocialInfo(socialId: String, socialType: BossAccountSocialType): BossAccount? {
        return mongoTemplate.findOne(Query()
            .addCriteria(Criteria.where("socialInfo.socialId").isEqualTo(socialId))
            .addCriteria(Criteria.where("socialInfo.socialType").isEqualTo(socialType))
        )
    }

    override fun existsBossAccountBySocialInfo(socialId: String, socialType: BossAccountSocialType): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(Criteria.where("socialInfo.socialId").isEqualTo(socialId))
            .addCriteria(Criteria.where("socialInfo.socialType").isEqualTo(socialType)), BossAccount::class.java
        )
    }

}
