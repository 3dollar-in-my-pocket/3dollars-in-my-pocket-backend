package com.depromeet.threedollar.document.boss.document.registration

import com.depromeet.threedollar.document.boss.document.registration.repository.RegistrationRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface RegistrationRepository : MongoRepository<Registration, String>, RegistrationRepositoryCustom
