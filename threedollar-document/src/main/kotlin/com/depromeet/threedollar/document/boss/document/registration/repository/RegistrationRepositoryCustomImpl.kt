package com.depromeet.threedollar.document.boss.document.registration.repository

import com.depromeet.threedollar.document.boss.document.account.BossAccountSocialType
import com.depromeet.threedollar.document.boss.document.registration.Registration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query

class RegistrationRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : RegistrationRepositoryCustom {

    override fun findRegistrationBySocialInfo(socialId: String, socialType: BossAccountSocialType): Registration? {
        return mongoTemplate.findOne(
            query(
                where("boss.socialInfo.socialId").`is`(socialId)
            ).addCriteria(
                where("boss.socialInfo.socialType").`is`(socialType)
            )
        )
    }

}
