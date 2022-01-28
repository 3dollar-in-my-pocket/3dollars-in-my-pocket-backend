package com.depromeet.threedollar.document.boss.document.account.repository

import com.depromeet.threedollar.document.boss.document.account.BossAccount
import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query

class BossAccountRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossAccountRepositoryCustom {

    override fun findBossAccountById(id: String): BossAccount? {
        return mongoTemplate.findById(id, BossAccount::class.java)
    }

    override fun findBossAccountBySocialInfo(socialId: String, socialType: BossAccountSocialType): BossAccount? {
        return mongoTemplate.findOne(
            query(
                where("socialInfo.socialId").`is`(socialId)
            ).addCriteria(
                where("socialInfo.socialType").`is`(socialType)
            ),
            BossAccount::class.java
        )
    }

    override fun existsBossAccountBySocialInfo(socialId: String, socialType: BossAccountSocialType): Boolean {
        return mongoTemplate.exists(
            query(
                where("socialInfo.socialId").`is`(socialId)
            ).addCriteria(
                where("socialInfo.socialType").`is`(socialType)
            ),
            BossAccount::class.java
        )
    }
}
