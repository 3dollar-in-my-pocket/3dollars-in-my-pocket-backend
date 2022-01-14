package com.depromeet.threedollar.document.boss.document.account.repository

import com.depromeet.threedollar.document.boss.document.account.BossAccount
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class BossAccountRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossAccountRepositoryCustom {

    override fun findBossAccountById(id: String): BossAccount? {
        return mongoTemplate.findById(id, BossAccount::class.java)
    }

    override fun findBossAccountBySocialInfo(socialId: String, socialType: BossAccountSocialType): BossAccount? {
        return mongoTemplate.findOne(
            Query.query(Criteria.where("socialId").`is`(socialId)),
            BossAccount::class.java
        )
    }

}
