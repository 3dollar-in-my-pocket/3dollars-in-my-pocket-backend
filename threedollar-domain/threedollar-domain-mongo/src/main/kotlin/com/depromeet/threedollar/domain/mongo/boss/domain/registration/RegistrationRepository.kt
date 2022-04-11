package com.depromeet.threedollar.domain.mongo.boss.domain.registration

import org.springframework.data.mongodb.repository.MongoRepository
import com.depromeet.threedollar.domain.mongo.boss.domain.registration.repository.RegistrationRepositoryCustom

interface RegistrationRepository : MongoRepository<Registration, String>, RegistrationRepositoryCustom
