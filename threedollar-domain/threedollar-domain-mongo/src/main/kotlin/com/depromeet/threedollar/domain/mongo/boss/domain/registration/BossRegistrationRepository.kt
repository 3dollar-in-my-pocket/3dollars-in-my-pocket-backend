package com.depromeet.threedollar.domain.mongo.boss.domain.registration

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.repository.BossRegistrationRepositoryCustom
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.repository.BossRegistrationStatisticsRepositoryCustom

interface BossRegistrationRepository : MongoRepository<BossRegistration, String>, BossRegistrationRepositoryCustom, BossRegistrationStatisticsRepositoryCustom
