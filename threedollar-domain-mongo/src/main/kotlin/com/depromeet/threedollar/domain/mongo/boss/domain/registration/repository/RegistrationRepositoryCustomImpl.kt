package com.depromeet.threedollar.domain.mongo.boss.domain.registration.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.registration.Registration
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationStatus
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo

class RegistrationRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : RegistrationRepositoryCustom {

    override fun existsRegistrationBySocialIdAndSocialType(socialId: String, socialType: com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(where("boss.socialInfo.socialId").isEqualTo(socialId))
            .addCriteria(where("boss.socialInfo.socialType").isEqualTo(socialType)), Registration::class.java
        )
    }

    override fun findWaitingRegistrationById(registrationId: String): Registration? {
        return mongoTemplate.findOne(Query()
            .addCriteria(Registration::id isEqualTo registrationId)
            .addCriteria(Registration::status isEqualTo RegistrationStatus.WAITING)
        )
    }

}
