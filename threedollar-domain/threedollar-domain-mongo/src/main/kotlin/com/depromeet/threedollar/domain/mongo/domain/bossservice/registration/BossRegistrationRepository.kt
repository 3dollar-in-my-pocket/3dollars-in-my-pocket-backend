package com.depromeet.threedollar.domain.mongo.domain.bossservice.registration

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.repository.BossRegistrationRepositoryCustom
import com.depromeet.threedollar.domain.mongo.domain.bossservice.registration.repository.BossRegistrationStatisticsRepositoryCustom

interface BossRegistrationRepository : MongoRepository<BossRegistration, String>, BossRegistrationRepositoryCustom, BossRegistrationStatisticsRepositoryCustom