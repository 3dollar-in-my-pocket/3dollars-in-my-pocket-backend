package com.depromeet.threedollar.domain.mongo

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.TestConstructor

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataMongoTest
internal abstract class IntegrationTest
