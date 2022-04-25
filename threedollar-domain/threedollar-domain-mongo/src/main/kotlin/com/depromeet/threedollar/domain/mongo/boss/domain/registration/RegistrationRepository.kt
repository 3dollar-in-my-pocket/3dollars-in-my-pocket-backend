package com.depromeet.threedollar.domain.mongo.boss.domain.registration

import com.depromeet.threedollar.domain.mongo.boss.domain.registration.repository.RegistrationRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface RegistrationRepository : MongoRepository<Registration, String>, RegistrationRepositoryCustom
