package com.depromeet.threedollar.domain.mongo.boss.domain.registration.repository

import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.Registration
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.RegistrationStatus
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo

class RegistrationRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : RegistrationRepositoryCustom {

    override fun existsWaitingRegistrationBySocialIdAndSocialType(socialId: String, socialType: BossAccountSocialType): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(where("boss.socialInfo.socialId").isEqualTo(socialId))
            .addCriteria(where("boss.socialInfo.socialType").isEqualTo(socialType))
            .addCriteria(Registration::status isEqualTo RegistrationStatus.WAITING), Registration::class.java
        )
    }

    override fun findWaitingRegistrationById(registrationId: String): Registration? {
        return mongoTemplate.findOne(Query()
            .addCriteria(Registration::id isEqualTo registrationId)
            .addCriteria(Registration::status isEqualTo RegistrationStatus.WAITING)
        )
    }

    override fun findWaitingRegistrationBySocialIdAndSocialType(socialId: String, socialType: BossAccountSocialType): Registration? {
        return mongoTemplate.findOne(Query()
            .addCriteria(where("boss.socialInfo.socialId").isEqualTo(socialId))
            .addCriteria(where("boss.socialInfo.socialType").isEqualTo(socialType))
            .addCriteria(Registration::status isEqualTo RegistrationStatus.WAITING)
        )
    }

    override fun findAllWaitingRegistrationsFromTheLatest(): List<Registration> {
        return mongoTemplate.find(Query()
            .addCriteria(Registration::status isEqualTo RegistrationStatus.WAITING)
            .with(Sort.by(Sort.Direction.DESC, Registration::id.name))
        )
    }

}
