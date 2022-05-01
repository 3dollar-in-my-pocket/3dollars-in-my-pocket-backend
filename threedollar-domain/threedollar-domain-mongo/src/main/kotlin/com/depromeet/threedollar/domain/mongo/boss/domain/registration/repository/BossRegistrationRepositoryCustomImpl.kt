package com.depromeet.threedollar.domain.mongo.boss.domain.registration.repository

import org.bson.types.ObjectId
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.lt
import com.depromeet.threedollar.domain.mongo.boss.domain.account.BossAccountSocialType
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistrationStatus
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.BossRegistration

class BossRegistrationRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : BossRegistrationRepositoryCustom {

    override fun existsWaitingRegistrationBySocialIdAndSocialType(socialId: String, socialType: BossAccountSocialType): Boolean {
        return mongoTemplate.exists(Query()
            .addCriteria(where("boss.socialInfo.socialId").isEqualTo(socialId))
            .addCriteria(where("boss.socialInfo.socialType").isEqualTo(socialType))
            .addCriteria(BossRegistration::status isEqualTo BossRegistrationStatus.WAITING), BossRegistration::class.java
        )
    }

    override fun findWaitingRegistrationById(registrationId: String): BossRegistration? {
        return mongoTemplate.findOne(Query()
            .addCriteria(BossRegistration::id isEqualTo registrationId)
            .addCriteria(BossRegistration::status isEqualTo BossRegistrationStatus.WAITING)
        )
    }

    override fun findWaitingRegistrationBySocialIdAndSocialType(socialId: String, socialType: BossAccountSocialType): BossRegistration? {
        return mongoTemplate.findOne(Query()
            .addCriteria(where("boss.socialInfo.socialId").isEqualTo(socialId))
            .addCriteria(where("boss.socialInfo.socialType").isEqualTo(socialType))
            .addCriteria(BossRegistration::status isEqualTo BossRegistrationStatus.WAITING)
        )
    }

    override fun findAllWaitingRegistrationsLessThanCursorOrderByLatest(cursor: String?, size: Int): List<BossRegistration> {
        if (cursor == null) {
            return mongoTemplate.find(Query()
                .addCriteria(BossRegistration::status isEqualTo BossRegistrationStatus.WAITING)
                .with(Sort.by(Sort.Direction.DESC, BossRegistration::id.name))
                .limit(size))
        }
        return mongoTemplate.find(Query()
            .addCriteria(BossRegistration::status isEqualTo BossRegistrationStatus.WAITING)
            .addCriteria(BossRegistration::id lt ObjectId(cursor))
            .with(Sort.by(Sort.Direction.DESC, BossRegistration::id.name))
            .limit(size))
    }

}
